package com.ak.jotit.feature.note.presentarion.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ak.jotit.core.navigation.NavigationType
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.presentarion.addeditnote.AddEditNoteScreen
import com.ak.jotit.feature.note.presentarion.addeditnote.NoteDetailScreen
import com.ak.jotit.feature.note.presentarion.notes.HomeScreen

@Composable
internal fun HomeRoute(
    navigationType: NavigationType,
    onAddNoteClick: () -> Unit
) {
    val viewModel: HomeActions = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    if (navigationType == NavigationType.PERMANENT_DRAWER) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            HomeScreen(
                uiState = uiState,
                actions = viewModel,
                navigationType = navigationType,
                onAddNoteClick = onAddNoteClick
            )
            AnimatedVisibility(visible = uiState.openedNote != null) {
                NoteDetailScreen(
                    noteColor = viewModel.color.value,
                    description = viewModel.description.value,
                    title = viewModel.title.value,
                    actions = viewModel
                )
            }
        }
    } else {
        HomeScreen(
            uiState = uiState,
            actions = viewModel,
            navigationType = navigationType,
            onAddNoteClick = onAddNoteClick
        )
    }
}
