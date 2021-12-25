package co.selim.sawmill.app

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

private const val DB_NAME = "sawmill"
private const val DB_USERNAME = "username"
private const val DB_PASSWORD = "password"

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {
    @LocalServerPort
    protected val port: Int = 0

    @Autowired
    private lateinit var flyway: Flyway

    @AfterEach
    fun resetDatabase() {
        flyway.clean()
        flyway.migrate()
    }

    companion object {
        @JvmStatic
        @Container
        protected var postgresqlContainer = PostgreSQLContainer("postgres:13.2-alpine")
            .apply {
                withDatabaseName(DB_NAME)
                withUsername(DB_USERNAME)
                withPassword(DB_PASSWORD)
            }

        @JvmStatic
        @BeforeAll
        fun configureDatabaseProperties() {
            System.setProperty("spring.flyway.url", postgresqlContainer.jdbcUrl)
            System.setProperty("spring.flyway.user", postgresqlContainer.username)
            System.setProperty("spring.flyway.password", postgresqlContainer.password)

            System.setProperty("spring.r2dbc.url", postgresqlContainer.jdbcUrl.replace("jdbc", "r2dbc"))
            System.setProperty("spring.r2dbc.username", postgresqlContainer.username)
            System.setProperty("spring.r2dbc.password", postgresqlContainer.password)
        }
    }

    protected val client by lazy {
        WebTestClient.bindToServer()
            .baseUrl("http://localhost:$port/api/v1/sawmills")
            .build()
    }
}