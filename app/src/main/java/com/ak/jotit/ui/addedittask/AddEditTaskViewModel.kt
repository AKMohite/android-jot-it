package com.ak.jotit.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.jotit.data.TaskDao
import com.ak.jotit.data.TaskEntity
import com.ak.jotit.ui.ADD_TASK_RESULT_OK
import com.ak.jotit.ui.EDIT_TASK_RESULT_OK
import com.ak.jotit.ui.tasks.TasksViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
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
        taskDao.insert(newTask)
//        navigate back
        _addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackResult(ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(updatedTask: TaskEntity) = viewModelScope.launch {
        taskDao.update(updatedTask)
//        navigate back
        _addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditTaskEvent()
        data class NavigateBackResult(val result: Int): AddEditTaskEvent()
    }

}