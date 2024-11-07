plugins {
    application
    alias(libs.plugins.kotlin)
}


group = "io.github.agentseek"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // LOGGING
    implementation(libs.kotlin.logging)
    implementation(libs.slf4j.simple)
    // REPL
    implementation(libs.picocli)
    implementation(libs.jline)
    // SERIALIZATION
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.module.kotlin)
    // COROUTINES
    implementation(libs.kotlinx.coroutines.core)

    // TESTING
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    // KOTEST
    testImplementation(libs.kotest.runner.junit5.jvm)
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
    jvmToolchain(libs.versions.kotlinJvmToolchain.get().toInt())
}