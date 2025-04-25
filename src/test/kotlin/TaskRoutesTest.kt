import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import routes.configureTaskRoutes
import com.example.model.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import model.Priority
import java.time.LocalDateTime
import io.ktor.server.routing.*


class TaskRoutesTest {

    private val sampleTasks = listOf(
        Task(id = 1, name = "Task 1", description = "Description 1", priority = Priority.Low, scheduledAt = LocalDateTime.now()),
        Task(id = 2, name = "Task 2", description = "Description 2", priority = Priority.Medium, scheduledAt = LocalDateTime.now())
    )

    private val testRepository = object : TaskRepository {
        override suspend fun allTasks(): List<Task> = sampleTasks
        override suspend fun taskById(id: Int): Task? = sampleTasks.find { it.id == id }
        override suspend fun tasksByPriority(priority: Priority): List<Task> = sampleTasks.filter { it.priority == priority }
        override suspend fun updateTask(id: Int, update: Task): Task? {
            assertEquals(1, id)
            assertEquals("Updated", update.name)
            return update.copy(id = id)
        }
        override suspend fun addTask(task: Task) {}
        override suspend fun removeTask(id: Int): Boolean = true
    }

    @Test
    fun testGetAllTasks() = testApplication {
        application {
            install(ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                configureTaskRoutes(testRepository)
            }
        }

        runTest {
            val response = client.get("/tasks")
            assertEquals(HttpStatusCode.OK, response.status)
            assertTrue(response.bodyAsText().contains("Task 1"))
            assertTrue(response.bodyAsText().contains("Task 2"))
        }
    }

    @Test
    fun testGetTaskById_valid() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                configureTaskRoutes(testRepository)
            }
        }

        runTest {
            val response = client.get("/tasks/1")
            assertEquals(HttpStatusCode.OK, response.status)
            assertTrue(response.bodyAsText().contains("Task 1"))
        }
    }

    @Test
    fun testGetTaskById_invalidIdFormat() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                configureTaskRoutes(testRepository)
            }
        }

        runTest {
            val response = client.get("/tasks/abc")
            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertTrue(response.bodyAsText().contains("Invalid or missing task ID"))
        }
    }

    @Test
    fun testGetTaskById_notFound() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                configureTaskRoutes(testRepository)
            }
        }

        runTest {
            val response = client.get("/tasks/999")
            assertEquals(HttpStatusCode.NotFound, response.status)
            assertTrue(response.bodyAsText().contains("Task not found"))
        }
    }

    @Test
    fun testGetTasksByPriority() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                configureTaskRoutes(testRepository)
            }
        }

        val response = client.get("/tasks/pr/Low")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testAddTask() = testApplication {
        val newTask = Task(
            name = "New Task",
            description = "Something to do",
            priority = Priority.Medium,
            scheduledAt = LocalDateTime.now()
        )

        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                configureTaskRoutes(testRepository)
            }
        }

        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(Task.serializer(), newTask))
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testUpdateTask() = testApplication {
        val existingTask = Task(id = 1, name = "Old", description = "Old desc", priority = Priority.Low, scheduledAt = LocalDateTime.now())
        val updatedTask = existingTask.copy(name = "Updated", description = "Updated desc")


        application {
            install(ContentNegotiation) { json() }
            routing { configureTaskRoutes(testRepository) }
        }

        val response = client.patch("/tasks/1") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(Task.serializer(), updatedTask))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Updated"))
    }

    @Test
    fun testDeleteTask() = testApplication {

        application {
            install(ContentNegotiation) { json() }
            routing { configureTaskRoutes(testRepository) }
        }

        val response = client.delete("/tasks/1")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}

