package com.ak.jotit.feature.note.presentarion.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ak.jotit.R
import com.ak.jotit.feature.note.domain.util.NoteOrderBy
import com.ak.jotit.feature.note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrderBy: NoteOrderBy = NoteOrderBy.Date(OrderType.Descending),
    onOrderChange: (NoteOrderBy) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = stringResource(R.string.order_title),
                isChecked = noteOrderBy is NoteOrderBy.Title,
                onCheck = { onOrderChange(NoteOrderBy.Title(noteOrderBy.orderType)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = stringResource(R.string.order_date),
                isChecked = noteOrderBy is NoteOrderBy.Date,
                onCheck = { onOrderChange(NoteOrderBy.Date(noteOrderBy.orderType)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = stringResource(R.string.order_color),
                isChecked = noteOrderBy is NoteOrderBy.Color,
                onCheck = { onOrderChange(NoteOrderBy.Color(noteOrderBy.orderType)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = stringResource(R.string.order_ascending),
                isChecked = noteOrderBy.orderType is OrderType.Ascending,
                onCheck = {
                    onOrderChange(noteOrderBy.copy(OrderType.Ascending))
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = stringResource(R.string.order_descending),
                isChecked = noteOrderBy.orderType is OrderType.Descending,
                onCheck = {
                    onOrderChange(noteOrderBy.copy(OrderType.Descending))
                }
            )
        }
    }
}