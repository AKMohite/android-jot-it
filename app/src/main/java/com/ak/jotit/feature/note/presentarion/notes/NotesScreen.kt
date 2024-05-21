package com.ak.jotit.feature.note.presentarion.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ak.jotit.R
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.presentarion.notes.components.NoteItem
import com.ak.jotit.feature.note.presentarion.notes.components.OrderSection
import com.ak.jotit.core.navigation.NavigationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun NotesScreen(
    state: NotesState,
    onAddEditClick: () -> Unit,
    toggleNotesFilter: () -> Unit,
    onFilterChange: (NoteOrderBy) -> Unit,
    onNoteClick: (Pair<String, String>) -> Unit,
    onDeleteNote: (id: String) -> Unit,
    onRestoreNote: () -> Unit,
    navigationType: NavigationType
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = navigationType == NavigationType.CLOSED_DRAWER) {
//                LargeFloatingActionButton(
//                    onClick = {  },
//                    modifier = Modifier
//                        .padding(16.dp),
//                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = stringResource(id = R.string.add_note),
//                        modifier = Modifier.size(28.dp)
//                    )
//                }
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
                    onClick = onAddEditClick,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
            }
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
                    onClick = toggleNotesFilter
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort),
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
                    onOrderChange = onFilterChange
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
                    scope,
                    snackbarHostState,
                    onNoteClick = onNoteClick,
                    onDeleteNote = onDeleteNote,
                    onRestoreNote = onRestoreNote
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
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onNoteClick: (Pair<String, String>) -> Unit,
    onDeleteNote: (id: String) -> Unit,
    onRestoreNote: () -> Unit
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
                        onNoteClick(Pair(note.id, note.color))
                    },
                note = note,
                onDeleteClick = {
                    onDeleteNote(note.id)
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note Deleted",
                            actionLabel = "Undo"
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            onRestoreNote()
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