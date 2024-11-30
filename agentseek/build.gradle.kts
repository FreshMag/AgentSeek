plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadowJar)
    application
}

group = "io.github.agentseek"
version = "fix: remove debug wait from asl agents."

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":engine"))
    implementation(libs.jason)

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

sourceSets {
    main {
        resources {
            srcDir("src/main/asl")
        }
    }
}

kotlin {
    jvmToolchain(libs.versions.kotlinJvmToolchain.get().toInt())
}