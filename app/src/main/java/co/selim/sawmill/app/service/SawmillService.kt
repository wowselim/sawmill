package co.selim.sawmill.app.service

import co.selim.sawmill.app.repository.SawmillEntity
import co.selim.sawmill.app.repository.SawmillRepository
import co.selim.sawmill.domain.Sawmill
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface SawmillService {

    fun save(name: String, city: String, country: String): Mono<Sawmill>

    fun update(sawmill: Sawmill): Mono<Sawmill>

    fun getById(id: UUID): Mono<Sawmill>

    fun getAll(): Flux<Sawmill>

    fun getAllWhereNameContains(name: String): Flux<Sawmill>
}

@Service
class ReactiveSawmillService(
    private val sawmillRepository: SawmillRepository
) : SawmillService {

    override fun save(name: String, city: String, country: String): Mono<Sawmill> {
        val entity = SawmillEntity(name = name, city = city, country = country)
        return sawmillRepository.save(entity)
            .map { it.toSawmill() }
    }

    override fun update(sawmill: Sawmill): Mono<Sawmill> {
        return sawmillRepository.save(sawmill.toSawmillEntity())
            .map { it.toSawmill() }

    }

    override fun getById(id: UUID): Mono<Sawmill> {
        return sawmillRepository.findById(id)
            .map { it.toSawmill() }
    }

    override fun getAll(): Flux<Sawmill> {
        return sawmillRepository.findAll()
            .map { it.toSawmill() }
    }

    override fun getAllWhereNameContains(name: String): Flux<Sawmill> {
        return sawmillRepository.findByNameContainingIgnoreCase(name)
            .map { it.toSawmill() }
    }

    private fun Sawmill.toSawmillEntity(): SawmillEntity {
        return SawmillEntity(id, name, city, country)
    }

    private fun SawmillEntity.toSawmill(): Sawmill {
        return Sawmill(id!!, name, city, country, createdAt, updatedAt)
    }
}
