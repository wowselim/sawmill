package co.selim.sawmill.domain

import java.time.LocalDateTime
import java.util.*

data class Sawmill(
    val id: UUID,
    val name: String,
    val city: String,
    val country: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
