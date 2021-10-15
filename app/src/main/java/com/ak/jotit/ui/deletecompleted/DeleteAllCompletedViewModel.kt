package com.ak.jotit.ui.deletecompleted

import androidx.lifecycle.ViewModel
import com.ak.jotit.di.ApplicationScope
import com.ak.jotit.feature.note.domain.repository.INotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(
    private val notesRepository: INotesRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        notesRepository.deleteCompletedNotes()
    }
}