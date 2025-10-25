package demo.at.ram.presentation.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import demo.at.ram.presentation.MainActivity
import demo.at.ram.presentation.R
import demo.at.ram.presentation.di.TestData
import demo.at.ram.presentation.toolkit.RamInstrumentation
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DetailsScreenTest01 {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun open_DetailsScreen_from_HomeScreen() {
        // Given
        val testCharacterId = TestData.characters[0].id
        val barItem1 = RamInstrumentation.getString(R.string.home)
        composeTestRule.onNodeWithText(barItem1).assertIsDisplayed()
        composeTestRule.onNodeWithText(barItem1).assertIsSelected()
        composeTestRule.onNodeWithTag("home_screen")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("item_$testCharacterId")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("details_screen")
            .assertIsNotDisplayed()

        // When
        composeTestRule.onNodeWithTag("item_$testCharacterId")
            .performClick()
        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithTag("details_screen")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_screen")
            .assertIsNotDisplayed()
    }
}