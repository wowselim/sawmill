package co.selim.sawmill.app

import co.selim.sawmill.app.controller.SawmillRequest
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.expectBodyList
import java.time.LocalDateTime
import java.util.*

class SawmillsEndpointTest : IntegrationTest() {
    private val sawmillRequestStub = SawmillRequest("Sägewerk am Main", "Frankfurt am Main", "Germany")

    @Test
    fun `there are two dummy sawmills on startup`() {
        client.get()
            .exchange()
            .expectBodyList<Any>()
            .hasSize(2)
    }

    @Test
    fun `sawmills can be filtered by name`() {
        client.get()
            .uri("/?name=sägewerk")
            .exchange()
            .expectBodyList<Any>()
            .hasSize(1)
    }

    @Test
    fun `creating a new sawmill returns the new sawmill`() {
        client.post()
            .bodyValue(sawmillRequestStub)
            .exchange()
            .expectBody()
            .jsonPath("$.id").value<String> { assertDoesNotThrow { UUID.fromString(it) } }
            .jsonPath("$.name").isEqualTo(sawmillRequestStub.name)
            .jsonPath("$.city").isEqualTo(sawmillRequestStub.city)
            .jsonPath("$.country").isEqualTo(sawmillRequestStub.country)
            .jsonPath("$.created_at").value<String> { assertDoesNotThrow { LocalDateTime.parse(it) } }
            .jsonPath("$.updated_at").value<String> { assertDoesNotThrow { LocalDateTime.parse(it) } }
    }

    @Test
    fun `new sawmills are persisted`() {
        client.post()
            .bodyValue(sawmillRequestStub)
            .exchange()
            .expectBody()
            .jsonPath("$.id").value<String> { id ->
                client.get()
                    .uri("/${id}")
                    .exchange()
                    .expectBody()
                    .jsonPath("$.id").value<String> { assertDoesNotThrow { UUID.fromString(it) } }
                    .jsonPath("$.name").isEqualTo(sawmillRequestStub.name)
                    .jsonPath("$.city").isEqualTo(sawmillRequestStub.city)
                    .jsonPath("$.country").isEqualTo(sawmillRequestStub.country)
                    .jsonPath("$.created_at").value<String> { assertDoesNotThrow { LocalDateTime.parse(it) } }
                    .jsonPath("$.updated_at").value<String> { assertDoesNotThrow { LocalDateTime.parse(it) } }
            }
    }

    @Test
    fun `updates to sawmills are persisted`() {
        val updatedName = "Updated name"

        client.post()
            .bodyValue(sawmillRequestStub)
            .exchange()
            .expectBody()
            .jsonPath("$.id").value<String> { id ->
                client.put()
                    .uri("/$id")
                    .bodyValue(sawmillRequestStub.copy(name = updatedName))
                    .exchange()
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(id)
                    .jsonPath("$.id").value<String> { assertDoesNotThrow { UUID.fromString(it) } }
                    .jsonPath("$.name").isEqualTo(updatedName)
                    .jsonPath("$.city").isEqualTo(sawmillRequestStub.city)
                    .jsonPath("$.country").isEqualTo(sawmillRequestStub.country)
                    .jsonPath("$.created_at").value<String> { assertDoesNotThrow { LocalDateTime.parse(it) } }
                    .jsonPath("$.updated_at").value<String> { assertDoesNotThrow { LocalDateTime.parse(it) } }

                client.get()
                    .uri("/$id")
                    .exchange()
                    .expectStatus().is2xxSuccessful
            }
    }

    @Test
    fun `get with bogus uuid returns 404`() {
        client.get()
            .uri("/b42631d4-dead-beef-9ee1-34950c9ee367")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `put with bogus uuid returns 404`() {
        client.put()
            .uri("/b42631d4-cafe-babe-9ee1-34950c9ee367")
            .bodyValue(sawmillRequestStub)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `get with invalid id returns 400`() {
        client.get()
            .uri("/kaboom")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `put with invalid id returns 400`() {
        client.put()
            .uri("/kaboom")
            .bodyValue(sawmillRequestStub)
            .exchange()
            .expectStatus().isBadRequest
    }
}