plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring") version "1.6.10"
}

dependencies {
    implementation(project(":domain"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(platform("org.testcontainers:testcontainers-bom:1.16.2"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.springframework:spring-jdbc")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:r2dbc")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
