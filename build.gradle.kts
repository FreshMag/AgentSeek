plugins {
    java
    kotlin("jvm") version "2.0.21"
    application
}

repositories {
    mavenCentral()
}

val jUnitVersion = "5.10.3"

dependencies {
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
    implementation("org.slf4j:slf4j-simple:2.0.13")


    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("io.github.agentseek.LaunchAgentSeek")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}
