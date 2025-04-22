package com.example

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*


fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/ktorevents_db",
        user = "postgres",
        password = "12345"
    )
}
