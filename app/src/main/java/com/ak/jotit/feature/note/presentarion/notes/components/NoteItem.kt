package com.ak.jotit.feature.note.presentarion.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.ak.jotit.feature.note.domain.model.NoteEntity
import com.ak.jotit.material2.JISwipeToDismiss
import com.ak.jotit.material2.SwipeActionsConfig
import com.ak.jotit.ui.theme.getNoteBgColors
import com.ak.jotit.ui.theme.getRandomColor

@Composable
fun NoteItem(
    note: NoteEntity,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cornerCutSize: Dp = 20.dp,
    onDeleteClick: () -> Unit
) {
    val bgColor = colorResource(id = (getNoteBgColors()[note.color] ?: getRandomColor()).colorRes).toArgb()
    JISwipeToDismiss(
        modifier = Modifier.fillMaxWidth(),
        endActionsConfig = SwipeActionsConfig(
            threshold = 0.1f,
            background = MaterialTheme.colorScheme.error,
            iconTint = MaterialTheme.colorScheme.onError,
            icon = Icons.Default.Delete,
            stayDismissed = true,
            onDismiss = onDeleteClick
        ),
//        showTutorial = index == 0
    ) {
        Box(
            modifier = modifier
        ) {
            NoteClippedCanvas(
                modifier = Modifier.matchParentSize(),
                bgColor = bgColor,
                cornerRadius = cornerRadius,
                cornerCutSize = cornerCutSize,
                canCutCornerSize = note.isSynced
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(end = 32.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = note.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun NoteClippedCanvas(
    modifier: Modifier = Modifier,
    bgColor: Int,
    cornerRadius: Dp,
    cornerCutSize: Dp,
    canCutCornerSize: Boolean = true
) {
    val clippedColor = MaterialTheme.colorScheme.onSurface.toArgb()
    Canvas(modifier = modifier) {
        val cutCornerSize = if (canCutCornerSize) 0.dp  else cornerCutSize
        val clipPath = Path().apply {
            lineTo(size.width - cutCornerSize.toPx(), 0f)
            lineTo(size.width, cutCornerSize.toPx())
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipPath(clipPath) {
            drawRoundRect(
                color = Color(bgColor),
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )

            drawRoundRect(
                color = Color(
                    ColorUtils.blendARGB(bgColor, clippedColor, 0.4f)
                ),
                topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
}
