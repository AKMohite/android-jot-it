package com.ak.jotit.feature.note.presentarion.addeditnote

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ak.jotit.R
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.feature.note.presentarion.addeditnote.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.title.value
    val descState = viewModel.description.value

    val snackbarHostState = remember { SnackbarHostState() }

    val noteBgAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.color.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UIAddEditEvent.SaveNote -> {
                    navController.navigateUp()
                }
                is AddEditNoteViewModel.UIAddEditEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                contentColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = stringResource(R.string.save_note))
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val padding = paddingValues.calculateTopPadding() + 8.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBgAnimatable.value)
                .padding(top = padding, start = padding, end = padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NoteEntity.noteColors.forEach{ color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 2.dp,
                                color = if (viewModel.color.value == colorInt) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBgAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = { title ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(title))
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(focusState))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = descState.text,
                hint = descState.hint,
                onValueChange = { desc ->
                    viewModel.onEvent(AddEditNoteEvent.EnteredDescription(desc))
                },
                onFocusChange = { focusState ->
                    viewModel.onEvent(AddEditNoteEvent.ChangeDescriptionFocus(focusState))
                },
                isHintVisible = descState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}