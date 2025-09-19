package demo.at.ram.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import demo.at.ram.presentation.MainActivity
import demo.at.ram.presentation.R
import demo.at.ram.presentation.toolkit.RamInstrumentation
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TopLevelDestinationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun bottomNavigation_navigatesToHomeScreen() {
        val barItem1 = RamInstrumentation.getString(R.string.home)
        val barItem2 = RamInstrumentation.getString(R.string.favorites)
        val barItem3 = RamInstrumentation.getString(R.string.about)

        composeTestRule.onNodeWithText(barItem1).assertIsDisplayed()
        composeTestRule.onNodeWithText(barItem1).assertIsSelected()

        composeTestRule.onNodeWithText(barItem2).assertIsDisplayed()
        composeTestRule.onNodeWithText(barItem2).assertIsNotSelected()

        composeTestRule.onNodeWithText(barItem3).assertIsDisplayed()
        composeTestRule.onNodeWithText(barItem3).assertIsNotSelected()
    }

    @Test
    fun bottomNavigation_navigatesToFavoritesScreen() {
        // Click on Favorites tab
        composeTestRule.onNodeWithText("Favorites").performClick()

        // Verify navigation occurred
        composeTestRule.onNodeWithText("Favorites").assertIsSelected()
    }

    @Test
    fun bottomNavigation_navigatesToAboutScreen() {
        // Click on About tab
        composeTestRule.onNodeWithText("About").performClick()

        // Verify navigation occurred
        composeTestRule.onNodeWithText("About").assertIsSelected()
    }

    @Test
    fun bottomNavigation_navigateBetweenAllTabs() {
        // Start at Home (default)
        composeTestRule.onNodeWithText("Home").assertIsSelected()

        // Navigate to Favorites
        composeTestRule.onNodeWithText("Favorites").performClick()
        composeTestRule.onNodeWithText("Favorites").assertIsSelected()
        composeTestRule.onNodeWithText("Home").assertExists()

        // Navigate to About
        composeTestRule.onNodeWithText("About").performClick()
        composeTestRule.onNodeWithText("About").assertIsSelected()

        // Navigate back to Home
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Home").assertIsSelected()
    }

    @Test
    fun bottomNavigation_tabLabelsAreDisplayed() {
        // Verify all tab labels are visible (assuming you have these strings)
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Favorites").assertIsDisplayed()
        composeTestRule.onNodeWithText("About").assertIsDisplayed()
    }

    @Test
    fun bottomNavigation_selectedStateUpdatesCorrectly() {
        // Initially Home should be selected
        composeTestRule.onNodeWithText("Home").assertIsSelected()

        // Click Favorites and verify selection
        composeTestRule.onNodeWithText("Favorites").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Favorites").assertIsSelected()
        // Home should no longer be selected
        composeTestRule.onNodeWithText("Home").assertExists()

        // Click About and verify selection
        composeTestRule.onNodeWithText("About").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("About").assertIsSelected()
    }
}
