package com.ak.jotit.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ak.jotit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TasksViewModel by viewModels()
}