package com.ak.jotit.ui.deletecompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.ak.jotit.di.ApplicationScope
import com.ak.jotit.repo.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val notesRepository: NotesRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        notesRepository.deleteCompletedNotes()
    }
}