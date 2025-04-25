package com.example

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*


fun Application.configureDatabases() {
    val dbConfig = environment.config.config("ktor.database")

    val url = dbConfig.property("url").getString()
    val user = dbConfig.property("user").getString()
    val password = dbConfig.property("password").getString()


    Database.connect(
        url = url,
        user = user,
        password = password
    )
}
