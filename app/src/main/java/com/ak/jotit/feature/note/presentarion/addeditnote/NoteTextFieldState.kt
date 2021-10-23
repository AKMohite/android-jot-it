package com.ak.jotit.feature.note.presentarion.addeditnote

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)