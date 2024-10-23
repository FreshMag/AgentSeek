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
    implementation("io.github.jason-lang:interpreter:3.2.0")

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
file(projectDir).listFiles().filter { it.extension == "mas2j" }.forEach { mas2jFile ->
    task<JavaExec>("run${mas2jFile.nameWithoutExtension}Mas") {
        classpath = sourceSets.getByName("main").runtimeClasspath
        mainClass.set("jason.infra.centralised.RunCentralisedMAS")
        args(mas2jFile.path)
        standardInput = System.`in`
        javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
    }
}

kotlin {
    jvmToolchain(17)
}