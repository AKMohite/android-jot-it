package com.ak.jotit.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ak.jotit.data.TaskEntity
import com.ak.jotit.databinding.ItemTaskBinding

class TasksAdapter: ListAdapter<TaskEntity, TasksAdapter.TasksViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    class TasksViewHolder(private val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(task: TaskEntity){
            binding.apply {
                checkboxComplete.isChecked = task.isComplete
                taskName.text = task.name
                taskName.paint.isStrikeThruText = task.isComplete
                labelPriority.isVisible = task.isImportant
            }
        }

    }

    class DiffCallBack: DiffUtil.ItemCallback<TaskEntity>(){
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean = oldItem == newItem
    }
}