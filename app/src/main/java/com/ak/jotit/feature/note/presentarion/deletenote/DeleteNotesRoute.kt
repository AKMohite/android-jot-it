package com.ak.jotit.feature.note.presentarion.deletenote

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ak.jotit.core.navigation.JotItContentType
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
internal fun DeleteNotesScreen() {}

@Composable
private fun SinglePaneDeleteNotesContent() {

}
