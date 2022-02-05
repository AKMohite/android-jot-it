package com.ak.jotit.feature.note.presentarion.notes


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ak.jotit.R
import com.ak.jotit.di.AppModule
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.ui.HomeActivity
import com.ak.jotit.ui.theme.JotItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HomeActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            JotItTheme {
                NavHost(
                    navController = navController,
                    startDestination = ScreenRoute.NotesScreen.route
                ) {
                    composable(route = ScreenRoute.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                }
            }
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun clickToggleOrderSection_isVisible() {
        val filterNote = composeRule.activity.getString(R.string.filter_note)
        val sortNotes = composeRule.activity.getString(R.string.sort_notes)
        composeRule
            .onNodeWithTag(filterNote)
            .assertDoesNotExist()
        composeRule
            .onNodeWithContentDescription(sortNotes)
            .performClick()

        composeRule
            .onNodeWithTag(filterNote)
            .assertIsDisplayed()
    }
}