package com.ak.jotit.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ak.jotit.data.PrefManager
import com.ak.jotit.data.SortOrder
import com.ak.jotit.data.TaskDao
import com.ak.jotit.data.TaskEntity
import com.ak.jotit.ui.ADD_TASK_RESULT_OK
import com.ak.jotit.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@FlowPreview
class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val prefManager: PrefManager,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = prefManager.preferencesFlow

    private val _tasksEventChannel = Channel<TasksEvent>()
    val tasksEventChannel = _tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ){ query, filterPreferences ->
        Pair(query, filterPreferences)
    }
        .flatMapLatest { (query, filterPreferences)->
            taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
        }

    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        prefManager.updateSortOrder(sortOrder)
    }

    fun onHideCompleteCheck(onHideComplete: Boolean) = viewModelScope.launch {
        prefManager.updateHideCompleted(onHideComplete)
    }

    fun onTaskCheckChanged(task: TaskEntity, isChecked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(isComplete = isChecked))
    }

    fun onTaskSelected(task: TaskEntity) = viewModelScope.launch {
        _tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskSwiped(task: TaskEntity) = viewModelScope.launch {
        taskDao.delete(task)
        _tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: TaskEntity) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        _tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when(result){
            ADD_TASK_RESULT_OK -> showTaskSaveConfirmationMessage("Task added")

            EDIT_TASK_RESULT_OK -> showTaskSaveConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSaveConfirmationMessage(msg: String) = viewModelScope.launch {
        _tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMsg(msg))
    }

    sealed class TasksEvent{
        data class ShowUndoDeleteTaskMessage(val task: TaskEntity): TasksEvent()
        object NavigateToAddTaskScreen: TasksEvent()
        data class NavigateToEditTaskScreen(val task: TaskEntity): TasksEvent()
        data class ShowTaskSavedConfirmationMsg(val msg: String): TasksEvent()
    }

}