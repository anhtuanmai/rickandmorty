package demo.at.ram.presentation.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import demo.at.ram.domain.model.Character
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
class DetailsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    lateinit var character : Character

    @Before
    fun setUp() {
        hiltRule.inject()

        val characterId = 1L
        character = TestData.characters.find { it.id == characterId }!!
    }

    @After
    fun tearDown() {
    }

    @Test
    fun open_DetailsScreen_from_HomeScreen() {
        // Given
        val barItem1 = RamInstrumentation.getString(R.string.home)
        composeTestRule.onNodeWithText(barItem1).assertIsDisplayed()
        composeTestRule.onNodeWithText(barItem1).assertIsSelected()

        // When
        composeTestRule.onNodeWithTag("characterList")
            .performScrollToNode(hasTestTag("item_${character.id}"))
            .performClick()

        // Then
        composeTestRule.onNodeWithText(character.name!!).assertIsDisplayed()
    }
}