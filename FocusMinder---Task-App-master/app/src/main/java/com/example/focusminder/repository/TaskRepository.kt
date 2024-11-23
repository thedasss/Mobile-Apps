package com.example.focusminder.repository

import com.example.focusminder.sharedpreferences.TaskSharedPreferences
import com.example.focusminder.model.Task

class TaskRepository(private val sharedPrefs: TaskSharedPreferences) {

    fun insertTask(task: Task) {
        sharedPrefs.insertTask(task)
    }

    fun deleteTask(task: Task) {
        sharedPrefs.deleteTask(task)
    }

    fun updateTask(task: Task) {
        sharedPrefs.updateTask(task)
    }

    fun getAllTasks(): List<Task> {
        return sharedPrefs.getAllTasks()
    }

    fun searchTask(query: String?): List<Task> {
        return sharedPrefs.searchTask(query)
    }
}
