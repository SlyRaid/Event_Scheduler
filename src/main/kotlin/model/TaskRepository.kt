package com.example.model

interface TaskRepository {
    suspend fun allTasks(): List<Task>
    suspend fun tasksByPriority(priority: Priority): List<Task>
    suspend fun taskById(id: Int): Task?
    suspend fun updateTask(id: Int, update: Task): Task?
    suspend fun addTask(task: Task)
    suspend fun removeTask(name: String): Boolean
}