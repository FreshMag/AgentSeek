plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/5.5.1/userguide/multi_project_builds.html
 */

rootProject.name = "AgentSeek"
include("engine")
include("agentseek")
