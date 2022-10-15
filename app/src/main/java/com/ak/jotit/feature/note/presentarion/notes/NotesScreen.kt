package com.ak.jotit.feature.note.presentarion.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ak.jotit.R
import com.ak.jotit.feature.note.domain.util.ScreenRoute
import com.ak.jotit.feature.note.presentarion.notes.components.NoteItem
import com.ak.jotit.feature.note.presentarion.notes.components.OrderSection
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(id = R.string.add_note)) },
                icon = {
//                    TODO remove AnimatedVisibility and uncomment expanded for Material 3
                    AnimatedVisibility(visible = listState.isScrollingUp()) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_note)
                        )
                    }
                },
//                expanded = listState.isScrollingUp(),
                onClick = {
                    navController.navigate(ScreenRoute.AddEditNoteScreen.route)
                },
                contentColor = MaterialTheme.colors.primary,
            )
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h4
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(state.notes) { note ->
                    NoteItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(ScreenRoute.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                            },
                        note = note,
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
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