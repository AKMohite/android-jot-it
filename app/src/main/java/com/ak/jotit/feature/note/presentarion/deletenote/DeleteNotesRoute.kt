package com.ak.jotit.feature.note.presentarion.deletenote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ak.jotit.core.navigation.JotItContentType
import com.ak.jotit.feature.note.presentarion.notes.components.NoteCard
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
internal fun DeleteNotesRoute(
    contentType: JotItContentType,
) {
    if (contentType == JotItContentType.DUAL_PANE) {
        TwoPane(
            first = {
                DeleteNotesScreen()
            },
            second = {
                 DeleteNoteDetailsScreen()
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = listOf() // TODO handle display features
        )
    } else {
        SinglePaneDeleteNotesContent()
    }
}

@Composable
internal fun DeleteNoteDetailsScreen() {}

@Composable
internal fun DeleteNotesScreen() {
//    NoteCard(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clip(RoundedCornerShape(4.dp))
//            .background(note.color)
//    )
}

@Composable
private fun SinglePaneDeleteNotesContent() {

}
