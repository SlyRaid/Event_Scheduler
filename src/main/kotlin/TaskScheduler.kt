package com.example.scheduler

import com.example.db.TaskDAO
import com.example.db.suspendTransaction
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds

object TaskScheduler {
    private val logger = LoggerFactory.getLogger(TaskScheduler::class.java)

    fun startChecking(intervalSeconds: Long = 10L): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                checkAndLogTasks()
                delay(intervalSeconds.seconds)
            }
        }
    }

    private suspend fun checkAndLogTasks() {
        val now = LocalDateTime.now()

        val triggeredTasks = suspendTransaction {
            TaskDAO.all().filter { it.scheduledAt <= now }
        }

        triggeredTasks.forEach {
            logger.info("!!! Событие наступило: (${it.name}) в ${it.scheduledAt}")
        }
    }
}
