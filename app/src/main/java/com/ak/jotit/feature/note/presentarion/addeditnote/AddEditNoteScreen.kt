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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ak.jotit.R
import com.ak.jotit.feature.note.presentarion.addeditnote.components.TransparentHintTextField
import com.ak.jotit.feature.note.presentarion.home.DetailActions
import com.ak.jotit.ui.theme.getNoteBgColors
import com.ak.jotit.ui.theme.getRandomColor
import kotlinx.coroutines.launch

@Composable
internal fun NoteDetailScreen(
    noteColor: String,
    description: NoteTextFieldState,
    title: NoteTextFieldState,
    actions: DetailActions
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val bgColor = colorResource(id = (getNoteBgColors()[noteColor] ?: getRandomColor()).colorRes).toArgb()
    val noteBgAnimatable = remember {
        Animatable(
            Color(bgColor)
        )
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = actions::saveNote,
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
                getNoteBgColors().forEach{ bgColor ->
                    val noteBgColor = colorResource(id = (getNoteBgColors()[bgColor.key] ?: getRandomColor()).colorRes).toArgb()
                    val colorInt = bgColor.value.colorRes
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = colorInt))
                            .border(
                                width = 2.dp,
                                color = if (noteColor == bgColor.key) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBgAnimatable.animateTo(
                                        targetValue = Color(noteBgColor),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                actions.onChangeColor(bgColor.key)
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = title.text,
                hint = title.hint,
                onValueChange = actions::onTitleChange,
                onFocusChange = actions::onTitleFocusChange,
                isHintVisible = title.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = description.text,
                hint = description.hint,
                onValueChange = actions::onDescChange,
                onFocusChange = actions::onDescFocusChange,
                isHintVisible = description.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteColor: String,
    description: NoteTextFieldState,
    title: NoteTextFieldState,
    onSaveNote: () -> Unit,
    onChangeColor: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onTitleFocusChange: (FocusState) -> Unit,
    onDescChange: (String) -> Unit,
    onDescFocusChange: (FocusState) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val bgColor = colorResource(id = (getNoteBgColors()[noteColor] ?: getRandomColor()).colorRes).toArgb()
    val noteBgAnimatable = remember {
        Animatable(
            Color(bgColor)
        )
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveNote,
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
                getNoteBgColors().forEach{ bgColor ->
                    val noteBgColor = colorResource(id = (getNoteBgColors()[bgColor.key] ?: getRandomColor()).colorRes).toArgb()
                    val colorInt = bgColor.value.colorRes
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = colorInt))
                            .border(
                                width = 2.dp,
                                color = if (noteColor == bgColor.key) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBgAnimatable.animateTo(
                                        targetValue = Color(noteBgColor),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                onChangeColor(bgColor.key)
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = title.text,
                hint = title.hint,
                onValueChange = onTitleChange,
                onFocusChange = onTitleFocusChange,
                isHintVisible = title.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = description.text,
                hint = description.hint,
                onValueChange = onDescChange,
                onFocusChange = onDescFocusChange,
                isHintVisible = description.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}