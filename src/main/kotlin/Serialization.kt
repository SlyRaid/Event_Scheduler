package com.example

import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(repository: TaskRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/tasks") {
            get {
                val tasks = repository.allTasks()
                call.respond(tasks)
            }

            get("/byId/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid or missing task ID")
                    return@get
                }

                val task = repository.taskById(id)
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                    return@get
                }

                call.respond(task)
            }

            get("/byPriority/{priority}") {
                val priorityAsText = call.parameters["priority"]
                if (priorityAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val priority = Priority.valueOf(priorityAsText)
                    val tasks = repository.tasksByPriority(priority)


                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(tasks)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                val task = call.receive<Task>()
                val createdEvent = repository.addTask(task)
                call.respond(HttpStatusCode.Created, createdEvent)
            }

            patch("/byId/{taskId}") {
                val id = call.parameters["taskId"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid or missing task ID")
                    return@patch
                }

                val updateRequest = try {
                    call.receive<Task>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request body")
                    return@patch
                }

                val updatedTask = repository.updateTask(id, updateRequest)

                if (updatedTask == null) {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                } else {
                    call.respond(updatedTask)
                }
            }

            delete("/{taskName}") {
                val name = call.parameters["taskName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.removeTask(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}