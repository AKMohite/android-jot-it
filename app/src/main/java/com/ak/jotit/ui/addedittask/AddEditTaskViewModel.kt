package com.ak.jotit.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.jotit.data.TaskEntity
import com.ak.jotit.repo.TasksRepository
import com.ak.jotit.ui.ADD_TASK_RESULT_OK
import com.ak.jotit.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val tasksRepository: TasksRepository,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val task = state.get<TaskEntity>("task") // this argument should be same in nav_graph

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private val _addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEventChannel = _addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick(){
        if (taskName.isBlank()) {
//            show invalid input msg
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, isImportant = taskImportance)
            updatedTask(updatedTask)
        } else {
            val newTask = TaskEntity(name = taskName, isImportant = taskImportance)
            createTask(newTask)
        }
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        _addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(msg))
    }

    private fun createTask(newTask: TaskEntity) = viewModelScope.launch {
        tasksRepository.insertTask(newTask)
//        navigate back
        _addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackResult(ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(updatedTask: TaskEntity) = viewModelScope.launch {
        tasksRepository.insertTask(updatedTask)
//        navigate back
        _addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditTaskEvent()
        data class NavigateBackResult(val result: Int): AddEditTaskEvent()
    }

}