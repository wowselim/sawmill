package co.selim.sawmill.app.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.*

interface SawmillRepository : ReactiveCrudRepository<SawmillEntity, UUID> {
    fun findByNameContainingIgnoreCase(name: String): Flux<SawmillEntity>
}
