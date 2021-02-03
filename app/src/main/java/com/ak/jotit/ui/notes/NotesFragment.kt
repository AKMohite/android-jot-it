package com.ak.jotit.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.jotit.R
import com.ak.jotit.data.SortOrder
import com.ak.jotit.data.NoteEntity
import com.ak.jotit.databinding.FragmentNotesBinding
import com.ak.jotit.util.exhaustive
import com.ak.jotit.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes), NotesAdapter.OnItemClickListener {

    private val viewModel: NotesViewModel by viewModels()

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNotesBinding.bind(view)
        val notesAdapter = NotesAdapter(this)
        setHasOptionsMenu(true)

        binding.apply {
            notesRecyclerView.apply {
                adapter = notesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = notesAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onNoteSwiped(note)
                }

            }).attachToRecyclerView(notesRecyclerView)

            fabAddNote.setOnClickListener {
                viewModel.onAddNewNoteClick()
            }
        }

        viewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
            notesAdapter.submitList(notes)
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notesEventChannel.collect { event ->
                when (event) {
                    is NotesViewModel.NotesEvent.ShowUndoDeleteNoteMessage -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.note_delete),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(getString(R.string.undo)) {
                                viewModel.onUndoDeleteClick(event.note)
                            }
                            .show()
                    }

                    is NotesViewModel.NotesEvent.NavigateToAddNoteScreen -> {
                        val action =
                            NotesFragmentDirections.actionNotesFragmentToFragmentAddEditNote(
                                null, getString(R.string.new_note)
                            )
                        findNavController().navigate(action)
                    }

                    is NotesViewModel.NotesEvent.NavigateToEditNoteScreen -> {
                        val action =
                            NotesFragmentDirections.actionNotesFragmentToFragmentAddEditNote(
                                event.note, getString(R.string.edit_note)
                            )
                        findNavController().navigate(action)
                    }

                    is NotesViewModel.NotesEvent.ShowNoteSavedConfirmationMsg -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }

                    is NotesViewModel.NotesEvent.NavigateToDeleteAllCompleteScreen -> {
                        val action =
                            NotesFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_home, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged { query ->
//            update search query
            viewModel.searchQuery.value = query
        }

        val pendingQuery = viewModel.searchQuery.value
        if (!pendingQuery.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

            viewLifecycleOwner.lifecycleScope.launch {
                menu.findItem(R.id.action_hide_completed_notes).isChecked =
                    viewModel.preferencesFlow.first().hideCompleted
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }

            R.id.action_sort_date -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }

            R.id.action_hide_completed_notes -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompleteCheck(item.isChecked)
                true
            }

            R.id.action_delete_completed_notes -> {
                viewModel.onDeleteAllCompleted()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(note: NoteEntity) {
        viewModel.onNoteSelected(note)
    }

    override fun onCheckBoxClick(note: NoteEntity, isChecked: Boolean) {
        viewModel.onNoteCheckChanged(note, isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}