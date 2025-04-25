package com.example.model

import com.example.eventservice.event.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import model.Priority
import java.time.LocalDateTime


@Serializable
data class Task(
    val id: Int? = null,
    val name: String,
    val description: String,
    val priority: Priority,
    @Serializable(with = LocalDateTimeSerializer::class)
    val scheduledAt: LocalDateTime
)