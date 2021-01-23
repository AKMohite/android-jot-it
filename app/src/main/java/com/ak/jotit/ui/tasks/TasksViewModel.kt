package com.ak.jotit.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ak.jotit.data.TaskDao
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@FlowPreview
class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
): ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val tasksFlow = searchQuery
        .debounce(300)
        .flatMapLatest { query->
            taskDao.getTasks(query)
        }

    val tasks = tasksFlow.asLiveData()


}