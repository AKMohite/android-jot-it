package com.ak.jotit.feature.note.presentarion.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ak.jotit.core.navigation.NavigationType
import com.ak.jotit.feature.note.presentarion.notes.HomeScreen

@Composable
internal fun HomeRoute(
    navigationType: NavigationType,
    onAddNoteClick: () -> Unit
) {
    val viewModel: HomeActions = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    if (navigationType == NavigationType.PERMANENT_DRAWER) {
        HomeScreen(
            uiState = uiState,
            actions = viewModel,
            navigationType = navigationType,
            onAddNoteClick = onAddNoteClick
        )
    } else {
        HomeScreen(
            uiState = uiState,
            actions = viewModel,
            navigationType = navigationType,
            onAddNoteClick = onAddNoteClick
        )
    }
}
