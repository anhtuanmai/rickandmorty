package demo.at.ram.data.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import tech.apter.junit.jupiter.robolectric.RobolectricExtension

@ExtendWith(RobolectricExtension::class)
@DisplayName("ConnectivityManagerHelper Tests")
class ConnectivityManagerHelperTest {

    private lateinit var connectivityManager: ConnectivityManager

    @BeforeEach
    fun setup() {
        // Mock the singleton object
        mockkObject(ConnectivityManagerHelper)

        // Create mock ConnectivityManager
        connectivityManager = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        // Important: unmock the object after each test
        unmockkObject(ConnectivityManagerHelper)
        clearAllMocks()
    }

    @Nested
    @DisplayName("When network is connected")
    inner class NetworkConnected {

        @Test
        @DisplayName("Returns true when network has internet capability")
        fun testNetworkWithInternetCapability() {
            // Given - mock network with internet capability
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
            }

            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertTrue(result)

            // Verify interactions
            verify { connectivityManager.activeNetwork }
            verify { connectivityManager.getNetworkCapabilities(mockNetwork) }
            verify { mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) }
        }

        @Test
        @DisplayName("Returns false when network has no internet capability")
        fun testNetworkWithoutInternetCapability() {
            // Given - network without internet capability
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
            }

            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertFalse(result)

            verify { connectivityManager.activeNetwork }
            verify { connectivityManager.getNetworkCapabilities(mockNetwork) }
        }
    }

    @Nested
    @DisplayName("When network is not available")
    inner class NetworkNotAvailable {

        @Test
        @DisplayName("Returns false when activeNetwork is null")
        fun testNoActiveNetwork() {
            // Given - no active network
            every { connectivityManager.activeNetwork } returns null

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertFalse(result)

            verify { connectivityManager.activeNetwork }
            verify(exactly = 0) { connectivityManager.getNetworkCapabilities(any()) }
        }

        @Test
        @DisplayName("Returns false when network capabilities is null")
        fun testNullNetworkCapabilities() {
            // Given - network exists but capabilities is null
            val mockNetwork = mockk<Network>()
            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns null

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertFalse(result)

            verify { connectivityManager.activeNetwork }
            verify { connectivityManager.getNetworkCapabilities(mockNetwork) }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    inner class EdgeCases {

        @Test
        @DisplayName("Handles exceptions gracefully")
        fun testExceptionHandling() {
            // Given - exception when getting network
            every { connectivityManager.activeNetwork } throws RuntimeException("Network error")

            // When/Then - should throw (or handle based on your error strategy)
            assertThrows<RuntimeException> {
                ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)
            }
        }

        @Test
        @DisplayName("Can be called multiple times with different results")
        fun testMultipleCalls() {
            // First call - connected
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
            }
            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            assertTrue(ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager))

            // Second call - disconnected
            every { connectivityManager.activeNetwork } returns null

            assertFalse(ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager))
        }

        @Test
        @DisplayName("Works with different ConnectivityManager instances")
        fun testDifferentManagerInstances() {
            // Given - two different managers
            val manager1 = mockk<ConnectivityManager>(relaxed = true)
            val manager2 = mockk<ConnectivityManager>(relaxed = true)

            val network1 = mockk<Network>()
            val capabilities1 = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
            }
            every { manager1.activeNetwork } returns network1
            every { manager1.getNetworkCapabilities(network1) } returns capabilities1

            every { manager2.activeNetwork } returns null

            // When
            val result1 = ConnectivityManagerHelper.isCurrentlyConnected(manager1)
            val result2 = ConnectivityManagerHelper.isCurrentlyConnected(manager2)

            // Then
            assertTrue(result1)
            assertFalse(result2)
        }
    }

    @Nested
    @DisplayName("Network Types")
    inner class NetworkTypes {

        @Test
        @DisplayName("Returns true for WiFi network with internet")
        fun testWiFiNetworkConnected() {
            // Given - WiFi network with internet
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
                every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
            }
            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertTrue(result)
        }

        @Test
        @DisplayName("Returns true for cellular network with internet")
        fun testCellularNetworkConnected() {
            // Given - Cellular network with internet
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
                every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
            }
            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertTrue(result)
        }

        @Test
        @DisplayName("Returns false for VPN without internet capability")
        fun testVPNWithoutInternet() {
            // Given - VPN without internet capability
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
                every { hasTransport(NetworkCapabilities.TRANSPORT_VPN) } returns true
            }
            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            // When
            val result = ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            assertFalse(result)
        }
    }

    @Nested
    @DisplayName("Verification Tests")
    inner class VerificationTests {

        @Test
        @DisplayName("Verifies correct method call sequence")
        fun testMethodCallSequence() {
            // Given
            val mockNetwork = mockk<Network>()
            val mockCapabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
            }
            every { connectivityManager.activeNetwork } returns mockNetwork
            every { connectivityManager.getNetworkCapabilities(mockNetwork) } returns mockCapabilities

            // When
            ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then - verify order of calls
            verifySequence {
                connectivityManager.activeNetwork
                connectivityManager.getNetworkCapabilities(mockNetwork)
                mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }

        @Test
        @DisplayName("Does not call getNetworkCapabilities when activeNetwork is null")
        fun testNoCapabilitiesCheckWhenNoNetwork() {
            // Given
            every { connectivityManager.activeNetwork } returns null

            // When
            ConnectivityManagerHelper.isCurrentlyConnected(connectivityManager)

            // Then
            verify(exactly = 1) { connectivityManager.activeNetwork }
            verify(exactly = 0) { connectivityManager.getNetworkCapabilities(any()) }
        }
    }
}
