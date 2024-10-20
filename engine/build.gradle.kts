plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "io.github.agentseek"
version = "unspecified"

repositories {
    mavenCentral()
}

val jUnitVersion = "5.10.3"

dependencies {
    // LOGGING
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    // REPL
    implementation("info.picocli:picocli:4.7.6")
    implementation("org.jline:jline:3.27.0")
    // SERIALIZATION
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    // COROUTINES
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // TESTING
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    // KOTEST
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.0")
}


tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}