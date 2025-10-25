package demo.at.ram.presentation.ui.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import demo.at.ram.presentation.designsystem.theme.RickAndMortyTheme
import demo.at.ram.presentation.di.TestData
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * requires `debugImplementation(libs.androidx.compose.ui.testManifest)` in `build.gradle` at module level
 */
@RunWith(AndroidJUnit4::class)
class DetailsScreenTest02 {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun detailsScreen_is_displayed() {
        // Given + When
        val character = TestData.characters[0]
        composeTestRule.setContent {
            RickAndMortyTheme {
                DetailsContent(DetailsUiState.Success(character, true)) {}
            }
        }

        // Then
        composeTestRule.onNodeWithTag("character_name")
            .assertTextContains(value = character.name!!, substring = true)
        composeTestRule.onNodeWithTag("character_status")
            .assertTextContains(value = character.status!!, substring = true)
        composeTestRule.onNodeWithTag("character_species")
            .assertTextContains(value = character.species!!, substring = true)
        composeTestRule.onNodeWithTag("character_gender")
            .assertTextContains(value = character.gender!!, substring = true)
        composeTestRule.onNodeWithTag("character_origin")
            .assertTextContains(value = character.origin!!.name!!, substring = true)
        composeTestRule.onNodeWithTag("character_location")
            .assertTextContains(value = character.location!!.name!!, substring = true)
    }
}