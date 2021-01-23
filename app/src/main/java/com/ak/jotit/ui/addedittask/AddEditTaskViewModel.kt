package com.ak.jotit.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ak.jotit.data.TaskDao
import com.ak.jotit.data.TaskEntity

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

}