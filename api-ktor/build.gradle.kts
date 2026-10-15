plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
    id("io.ktor.plugin") version "3.1.3"
}

group = "replay"
version = "0.1.0"

application {
    mainClass.set("replay.ApplicationKt")
}

kotlin { jvmToolchain(21) }

repositories { mavenCentral() }

val ktorVersion = "3.1.3"
val exposedVersion = "0.61.0"

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    // OpenAPI gerado do código + Swagger UI (smiley4)
    implementation("io.github.smiley4:ktor-openapi:5.0.0")
    implementation("io.github.smiley4:ktor-swagger-ui:5.0.0")

    // Persistência
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.flywaydb:flyway-core:11.8.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.8.0")

    implementation("ch.qos.logback:logback-classic:1.5.18")
}
