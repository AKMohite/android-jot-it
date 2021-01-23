package com.ak.jotit.ui.deletecompleted

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ak.jotit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment: DialogFragment() {

    private val viewModel: DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.delete_all_msg))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.yes)){ _, _ ->
//                call viewModel
                viewModel.onConfirmClick()
            }
            .create()
}