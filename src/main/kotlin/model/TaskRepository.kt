package com.example.model

import model.Priority

interface TaskRepository {
    suspend fun allTasks(): List<Task>
    suspend fun tasksByPriority(priority: Priority): List<Task>
    suspend fun taskById(id: Int): Task?
    suspend fun updateTask(id: Int, update: Task): Task?
    suspend fun addTask(task: Task)
    suspend fun removeTask(id: Int): Boolean
}