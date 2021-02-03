package com.ak.jotit.ui.addeditnote

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ak.jotit.R
import com.ak.jotit.databinding.FragmentAddEditNoteBinding
import com.ak.jotit.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private val viewModel: AddEditNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditNoteBinding.bind(view)

        binding.apply {
            noteTitleEt.setText(viewModel.noteTitle)

            noteTitleEt.addTextChangedListener {
                viewModel.noteTitle = it.toString()
            }

            fabSaveNote.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditNoteEventChannel.collect { event ->
                when(event){
                    is AddEditNoteViewModel.AddEditNoteEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateBackResult -> {
                        binding.noteTitleEt.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}