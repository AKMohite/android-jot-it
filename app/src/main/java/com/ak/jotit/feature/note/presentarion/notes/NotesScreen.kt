package com.ak.jotit.feature.note.presentarion.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ak.jotit.R
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.notes.components.NoteItem
import com.ak.jotit.feature.note.presentarion.notes.components.OrderSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(id = R.string.add_note)) },
                icon = {
//                    TODO remove AnimatedVisibility and uncomment expanded for Material 3
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_note)
                    )
                },
                expanded = listState.isScrollingUp(),
                onClick = {
                    navController.navigate(ScreenRoute.AddEditNoteScreen.route)
                },
                contentColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val padding = paddingValues.calculateTopPadding() + 8.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding, start = padding, end = padding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = stringResource(R.string.sort_notes)
                    )
                }
            }

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .testTag(stringResource(id = R.string.filter_note)),
                    noteOrderBy = state.noteOrderBy,
                    onOrderChange = { order ->
                        viewModel.onEvent(NotesEvent.OrderNotes(order))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (state.notes.isEmpty()) {
                EmptyState()
            }
            else {
                NotesList(
                    listState,
                    state.notes,
                    navController,
                    viewModel,
                    scope,
                    snackbarHostState
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
        Column(
            Modifier
                .fillMaxSize()
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.sad_face), fontSize = 72.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.notes_empty_text), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun NotesList(
    listState: LazyListState,
    targetState: List<NoteEntity>,
    navController: NavController,
    viewModel: NotesViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
    ) {
        items(
            key = { note -> note.id },
            items = targetState
        ) { note ->
            NoteItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(ScreenRoute.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                    },
                note = note,
                onDeleteClick = {
                    viewModel.onEvent(NotesEvent.DeleteNote(note))
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note Deleted",
                            actionLabel = "Undo"
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(NotesEvent.RestoreNote)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) {
        mutableStateOf(firstVisibleItemIndex)
    }
    var previousScrollOffset by remember(this) {
        mutableStateOf(firstVisibleItemScrollOffset)
    }

    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}