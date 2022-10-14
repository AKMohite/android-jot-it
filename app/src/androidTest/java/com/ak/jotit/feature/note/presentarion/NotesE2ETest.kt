package com.ak.jotit.feature.note.presentarion

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.ak.jotit.R
import com.ak.jotit.di.AppModule
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteScreen
import com.ak.jotit.feature.note.presentarion.notes.NotesScreen
import com.ak.jotit.ui.HomeActivity
import com.ak.jotit.ui.theme.JotItTheme
import com.ak.jotit.util.DESCRIPTION_FIELD
import com.ak.jotit.util.TITLE_FIELD
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HomeActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            JotItTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenRoute.NotesScreen.route
                ) {
                    composable(
                        route = ScreenRoute.NotesScreen.route
                    ) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = ScreenRoute.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.StringType
                                defaultValue = ""
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) { entry ->
                        val color = entry.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {
        val addNote = composeRule.activity.getString(R.string.add_note)
        val titleTextField = TITLE_FIELD
        val descTextField = DESCRIPTION_FIELD
        val saveNote = composeRule.activity.getString(R.string.save_note)

//        notes screen
        composeRule
            .onNodeWithContentDescription(addNote)
            .performClick()

//        add edit notes screen
        composeRule
            .onNodeWithTag(titleTextField)
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag(descTextField)
            .performTextInput("test-desc")
        composeRule
            .onNodeWithContentDescription(saveNote)
            .performClick()

//        notes screen
        composeRule
            .onNodeWithText("test-title")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("test-title")
            .performClick()

//        add edit screen
        composeRule
            .onNodeWithTag(titleTextField)
            .assertTextEquals("test-title")
        composeRule
            .onNodeWithTag(descTextField)
            .assertTextEquals("test-desc")
        composeRule
            .onNodeWithTag(titleTextField)
            .performTextInput("-2")
        composeRule
            .onNodeWithContentDescription(saveNote)
            .performClick()

//        notes screen
        composeRule
            .onNodeWithText("test-title-2")
            .assertIsDisplayed()

    }
}