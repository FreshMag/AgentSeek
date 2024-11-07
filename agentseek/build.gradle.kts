plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadowJar)
    application
}

group = "io.github.agentseek"
version = "1.0.0"

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
    jvmToolchain(libs.versions.kotlinJvmToolchain.get().toInt())
}