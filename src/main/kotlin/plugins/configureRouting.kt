package plugins

import com.example.model.TaskRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.configureTaskRoutes


fun Application.configureRouting(repository: TaskRepository) {
    routing {
        configureTaskRoutes(repository)
    }
}