package com.example.focusminder.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.focusminder.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskSharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun insertTask(task: Task) {
        val tasksList = getAllTasks().toMutableList()
        tasksList.add(task)
        saveTasks(tasksList)
    }
    fun deleteTask(task: Task) {
        val tasksList = getAllTasks().toMutableList()
        tasksList.remove(task)
        saveTasks(tasksList)
    }

    fun updateTask(updatedTask: Task) {
        val tasksList = getAllTasks().toMutableList()
        val index = tasksList.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasksList[index] = updatedTask
            saveTasks(tasksList)
        }
    }
    fun getAllTasks(): List<Task> {
        val json = sharedPreferences.getString("tasks", "[]")
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun searchTask(query: String?): List<Task> {
        return getAllTasks().filter {
            it.taskTitle.contains(query ?: "", ignoreCase = true) ||
                    it.taskDesc.contains(query ?: "", ignoreCase = true)
        }
    }

    private fun saveTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        sharedPreferences.edit().putString("tasks", json).apply()
    }

    fun addTask(task: Task) {
        // Retrieve the existing tasks from SharedPreferences
        val tasks = getAllTasks().toMutableList()

        // Generate a new id based on the highest existing id
        val newId = if (tasks.isNotEmpty()) tasks.maxOf { it.id } + 1 else 1
        val newTask = task.copy(id = newId)

        // Add the new task with the generated id
        tasks.add(newTask)
        saveTasks(tasks) // Save the updated task list back to SharedPreferences
    }

}
