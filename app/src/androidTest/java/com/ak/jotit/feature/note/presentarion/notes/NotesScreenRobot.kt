package com.ak.jotit.feature.note.presentarion.notes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.ak.jotit.ui.HomeActivity
import com.ak.jotit.R

typealias HomeActivityRule = AndroidComposeTestRule<ActivityScenarioRule<HomeActivity>, HomeActivity>

internal fun launchNotesScreen(
    notesTestRule: HomeActivityRule,
    block: NotesScreenRobot.() -> Unit
): NotesScreenRobot {
//    you can launch other screen here before
    return NotesScreenRobot(notesTestRule).apply(block)
}

internal class NotesScreenRobot(
    private val rule: HomeActivityRule
) {

    fun tapOnFilterIcon() {
        val sortNotes = rule.activity.getString(R.string.sort_notes)
        rule
            .onNodeWithContentDescription(sortNotes)
            .performClick()
    }
    fun createNewNote() {
        val createNewNote = rule.activity.getString(R.string.add_note)
        rule.onNodeWithTag(createNewNote).performClick()
    }

    infix fun verify(
        block: NotesVerificationRobot.() -> Unit
    ): NotesVerificationRobot {
        return NotesVerificationRobot(rule).apply(block)
    }
}

internal class NotesVerificationRobot(
    private val rule: HomeActivityRule
) {

    fun emptyStateIsDisplayed() {
        val emptyMessage = rule.activity.getString(R.string.notes_empty_text)
        val sadFace = rule.activity.getString(R.string.sad_face)
        rule.onNodeWithText(emptyMessage)
            .assertIsDisplayed()
        rule.onNodeWithText(sadFace)
            .assertIsDisplayed()
    }

    fun filtersAreNotDisplayed() {
        val filterNote = rule.activity.getString(R.string.filter_note)
        rule.onNodeWithTag(filterNote)
            .assertDoesNotExist()
    }

    fun filtersAreDisplayed() {
        val filterNote = rule.activity.getString(R.string.filter_note)
        rule.onNodeWithTag(filterNote)
            .assertIsDisplayed()
    }
}