plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "io.github.agentseek"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":engine"))

    testImplementation(kotlin("test"))
}


application {
    mainClass.set("io.github.agentseek.AgentSeek")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}