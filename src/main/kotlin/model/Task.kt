package com.example.model


import com.example.eventservice.event.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

enum class Priority {
    Low, Medium, High, Vital
}

@Serializable
data class Task(
    val name: String,
    val description: String,
    val priority: Priority,
    @Serializable(with = LocalDateTimeSerializer::class)
    val scheduledAt: LocalDateTime
)