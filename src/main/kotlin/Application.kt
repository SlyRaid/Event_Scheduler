package com.example

import db.PostgresTaskRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import plugins.configureRouting
import scheduler.TaskScheduler

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = PostgresTaskRepository()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }

    configureSerialization(repository)
    configureDatabases()
    configureRouting(repository)

    TaskScheduler.startChecking(intervalSeconds = 5L)
}


