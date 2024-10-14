plugins {
    java
    application
    id( "org.openjfx.javafxplugin" ) version "0.0.13"
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "swing",
    "graphics"
)

val supportedPlatforms = listOf("linux", "mac", "win")

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.swing")
}

val jUnitVersion = "5.7.1"

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("ryleh.LaunchRyleh")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
