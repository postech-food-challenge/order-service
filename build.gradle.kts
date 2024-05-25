val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val hikaricp_version: String by project
val koin_version: String by project
val exposed_version: String by project
val postgresql_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("org.sonarqube") version "4.4.1.3373"
    id("jacoco")
}

group = "br.com.fiap.postech"
version = "0.0.1"

application {
    mainClass.set("br.com.fiap.postech.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sonar {
    properties {
        property("sonar.gradle.skipCompile", "true")
        property("sonar.projectKey", "postech-food-challenge_order-service")
        property("sonar.organization", "postech-food-challenge")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property(
            "sonar.coverage.exclusions",
            """
                **/br/com/fiap/postech/Application.kt,
                **/br/com/fiap/postech/domain/**,
                **/br/com/fiap/postech/configuration/**,
                **/br/com/fiap/postech/infrastructure/**
            """.trimIndent()
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty:1.6.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.valiktor:valiktor-core:0.12.0")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("org.postgresql:postgresql:$postgresql_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/br/com/fiap/postech/domain/**",
                    "**/br/com/fiap/postech/configuration/**",
                    "**/br/com/fiap/postech/infrastructure/**"
                )
            }
        })
    )
}