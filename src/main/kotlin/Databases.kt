package com.example

import USER
import PASSWORD
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*


fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/ktorevents_db",
        user = USER,
        password = PASSWORD
    )
}
