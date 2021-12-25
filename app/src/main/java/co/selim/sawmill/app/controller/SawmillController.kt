package co.selim.sawmill.app.controller

import co.selim.sawmill.app.service.SawmillService
import co.selim.sawmill.domain.Sawmill
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@RestController
@RequestMapping("/api/v1/sawmills")
class SawmillController(private val sawmillService: SawmillService) {

    @PostMapping
    fun createSawmill(@RequestBody sawmill: SawmillRequest): Mono<Sawmill> {
        return sawmillService.save(sawmill.name, sawmill.city, sawmill.country)
    }

    @GetMapping
    fun getAll(@RequestParam(required = false) name: String?): Flux<Sawmill> {
        return when (name) {
            null -> sawmillService.getAll()
            else -> sawmillService.getAllWhereNameContains(name)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): Mono<ResponseEntity<Sawmill>> {
        return sawmillService.getById(id)
            .map { ResponseEntity.ok(it) }
            .switchIfEmpty { Mono.just(ResponseEntity.notFound().build()) }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody sawmill: SawmillRequest): Mono<ResponseEntity<Sawmill>> {
        return sawmillService.getById(id)
            .flatMap {
                val updatedSawmill = it.copy(name = sawmill.name, city = sawmill.city, country = sawmill.country)
                sawmillService.update(updatedSawmill)
            }
            .map { ResponseEntity.ok(it) }
            .switchIfEmpty { Mono.just(ResponseEntity.notFound().build()) }
    }
}