package db

import com.example.db.TaskDAO
import com.example.db.TaskTable
import com.example.db.daoToModel
import com.example.db.suspendTransaction
import com.example.model.Task
import com.example.model.TaskRepository
import model.Priority


class PostgresTaskRepository : TaskRepository {
    override suspend fun allTasks(): List<Task> = suspendTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    override suspend fun tasksByPriority(priority: Priority): List<Task> = suspendTransaction {
        TaskDAO
            .find { (TaskTable.priority eq priority.toString()) }
            .map(::daoToModel)
    }

    override suspend fun taskById(id: Int): Task? = suspendTransaction {
        TaskDAO
            .find { (TaskTable.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun updateTask(id: Int, update: Task): Task? = suspendTransaction {
        val taskDao = TaskDAO.findById(id) ?: return@suspendTransaction null

        update.name.let { taskDao.name = it }
        update.description.let { taskDao.description = it }
        update.priority.let { taskDao.priority = it.toString() }
        update.scheduledAt.let { taskDao.scheduledAt = it }

        daoToModel(taskDao)
    }

    override suspend fun addTask(task: Task): Unit = suspendTransaction {
        TaskDAO.new {
            name = task.name
            description = task.description
            priority = task.priority.toString()
            scheduledAt = task.scheduledAt
        }
    }

    override suspend fun removeTask(id: Int): Boolean = suspendTransaction {
        val taskDao = TaskDAO.findById(id)
        if (taskDao != null) {
            taskDao.delete()
            true
        } else {
            false
        }
    }
}