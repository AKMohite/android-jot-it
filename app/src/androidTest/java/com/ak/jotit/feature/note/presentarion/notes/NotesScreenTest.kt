package com.ak.jotit.feature.note.presentarion.notes


import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.ak.jotit.di.AppModule
import com.ak.jotit.ui.HomeActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
    }

    @After
    fun tearDown() {}

    @Test
    fun initialState_emptyStateIsVisible() {
        launchNotesScreen(notesTestRule = composeRule) {
//            no operation
        } verify {
            filtersAreNotDisplayed()
            emptyStateIsDisplayed()
        }
    }

    @Test
    fun clickToggleOrderSection_isVisible() {
        launchNotesScreen(notesTestRule = composeRule) {
            tapOnFilterIcon()
        } verify {
            filtersAreDisplayed()
        }
    }
}