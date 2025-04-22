package com.example

import com.example.model.PostgresTaskRepository
import com.example.scheduler.TaskScheduler
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = PostgresTaskRepository()

    configureSerialization(repository)
    configureDatabases()
    configureRouting()

    TaskScheduler.startChecking(intervalSeconds = 5L)
}
