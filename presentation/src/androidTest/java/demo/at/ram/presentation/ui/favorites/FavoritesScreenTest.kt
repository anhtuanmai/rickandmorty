package demo.at.ram.presentation.ui.favorites

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import demo.at.ram.presentation.designsystem.theme.RickAndMortyTheme
import demo.at.ram.presentation.di.TestData
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {
    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    /**
     * tests [demo.at.ram.presentation.ui.home.CharacterCard]
     * and [demo.at.ram.presentation.ui.home.CharacterCardList]
     */
    @Test
    fun show_FavoritesContent_with_items() {
        // Given + When
        val character = TestData.characters
        composeTestRule.setContent {
            RickAndMortyTheme {
                FavoritesContent(FavoritesUiState.Success(character)) {}
            }
        }

        // Then
        composeTestRule.onNodeWithTag("characterList")
            .assertIsDisplayed()
    }

}