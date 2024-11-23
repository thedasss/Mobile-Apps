package com.example.focusminder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.focusminder.databinding.LayoutBinding
import com.example.focusminder.fragments.HomeFragmentDirections
import com.example.focusminder.model.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val taskBinding: LayoutBinding) : RecyclerView.ViewHolder(taskBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Compare by unique identifier (id)
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Compare the entire content of the Task
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = differ.currentList[position]

        holder.apply {
            taskBinding.noteTitle.text = currentTask.taskTitle
            taskBinding.noteDesc.text = currentTask.taskDesc

            itemView.setOnClickListener { view ->
                val direction = HomeFragmentDirections.actionHomeFragmentToEditFragment(currentTask)
                view.findNavController()?.navigate(direction)
            }
        }
    }
}
