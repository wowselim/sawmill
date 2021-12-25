package co.selim.sawmill.app.repository

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("sawmills")
data class SawmillEntity(
    @Id
    val id: UUID? = null,
    val name: String,
    val city: String,
    val country: String,
) {
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
