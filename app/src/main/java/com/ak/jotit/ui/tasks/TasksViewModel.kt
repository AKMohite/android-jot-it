package com.ak.jotit.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ak.jotit.data.PrefManager
import com.ak.jotit.data.SortOrder
import com.ak.jotit.data.TaskDao
import com.ak.jotit.data.TaskEntity
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
    private val prefManager: PrefManager
): ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = prefManager.preferencesFlow

    private val _tasksEventChannel = Channel<TasksEvent>()
    val tasksEventChannel = _tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery,
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

    fun onTaskSelected(task: TaskEntity){

    }

    fun onTaskSwiped(task: TaskEntity) = viewModelScope.launch {
        taskDao.delete(task)
        _tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: TaskEntity) = viewModelScope.launch {
        taskDao.insert(task)
    }

    sealed class TasksEvent{
        data class ShowUndoDeleteTaskMessage(val task: TaskEntity): TasksEvent()
    }

}