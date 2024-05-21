package com.ak.jotit.feature.note.presentarion.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.layout.DisplayFeature
import com.ak.jotit.core.navigation.JotItContentType
import com.ak.jotit.core.navigation.NavigationType
import com.ak.jotit.feature.note.presentarion.addeditnote.NoteDetailScreen
import com.ak.jotit.feature.note.presentarion.notes.HomeScreen
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
internal fun HomeRoute(
    navigationType: NavigationType,
    contentType: JotItContentType,
    onAddNoteClick: () -> Unit,
) {
    val viewModel: HomeActions = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    if (contentType == JotItContentType.DUAL_PANE) {
        TwoPane(
            first = {
                HomeScreen(
                    uiState = uiState,
                    actions = viewModel,
                    navigationType = navigationType,
                    onAddNoteClick = {
                        throw IllegalStateException("this click event should not happen in dual pane")
                    }
                )
            },
            second = {
                AnimatedVisibility(visible = uiState.openedNote != null) {
                    NoteDetailScreen(
                        note = uiState.getNoteDetail(),
                        description = viewModel.description.value,
                        title = viewModel.title.value,
                        actions = viewModel
                    )
                }
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = listOf() // TODO handle display features
        )
    } else {
        HomeSinglePaneContent(
            uiState = uiState,
            actions = viewModel,
            navigationType = navigationType,
            onAddNoteClick = onAddNoteClick,
            onBackPress = viewModel::closeDetail
        )
    }
}

@Composable
private fun HomeSinglePaneContent(
    uiState: HomeState,
    actions: HomeActions,
    navigationType: NavigationType,
    onAddNoteClick: () -> Unit,
    onBackPress: () -> Unit
) {
    if (uiState.canShowDetail()) {
        BackHandler {
            onBackPress()
        }
        NoteDetailScreen(
            note = uiState.getNoteDetail(),
            description = actions.description.value,
            title = actions.title.value,
            actions = actions
        )
    } else {
        HomeScreen(
            uiState = uiState,
            actions = actions,
            navigationType = navigationType,
            onAddNoteClick = onAddNoteClick
        )
    }
}
