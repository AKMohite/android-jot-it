package com.ak.jotit.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.jotit.data.NoteEntity
import com.ak.jotit.databinding.ItemNoteBinding

class NotesAdapter(private val listener: OnItemClickListener): ListAdapter<NoteEntity, NotesAdapter.NotesViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote)
    }

    inner class NotesViewHolder(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val  note = getItem(position)
                        listener.onItemClick(note)
                    }
                }

                checkboxComplete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val  note = getItem(position)
                        listener.onCheckBoxClick(note, checkboxComplete.isChecked)
                    }
                }
            }
        }

        fun bind(note: NoteEntity) = with(binding){
            checkboxComplete.isChecked = note.isComplete
            noteTitle.text = note.name
            noteTitle.paint.isStrikeThruText = note.isComplete
            labelPriority.isVisible = note.isImportant
        }

    }

    interface OnItemClickListener{
        fun onItemClick(note: NoteEntity)
        fun onCheckBoxClick(note: NoteEntity, isChecked: Boolean)
    }

    class DiffCallBack: DiffUtil.ItemCallback<NoteEntity>(){
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean = oldItem == newItem
    }
}