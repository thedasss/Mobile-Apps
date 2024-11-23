package com.example.focusminder.viewmodel

import androidx.lifecycle.ViewModel
import com.example.focusminder.model.Task
import com.example.focusminder.repository.TaskRepository

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun addTask(task: Task) {
        taskRepository.insertTask(task)
    }

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    fun getAllTasks(): List<Task> {
        return taskRepository.getAllTasks()
    }

    fun searchTask(query: String?): List<Task> {
        return taskRepository.searchTask(query)
    }
}
