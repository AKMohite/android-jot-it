package com.ak.jotit.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ak.jotit.R
import com.ak.jotit.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAddEditTask : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            taskNameEt.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            dateCreatedTv.isVisible = viewModel.task != null
            dateCreatedTv.text = "Created at: ${viewModel.task?.createdDateFormatted}"
        }
    }
}