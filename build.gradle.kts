import java.text.SimpleDateFormat
import java.util.Date
/**
 * Usage:
 * ```bash
 * ./gradlew changelog -Pversion="1.0.4" -PchangelogText="Added new feature."
 * ```
 */
tasks.register("updateVersionAndChangelog") {
    description = "Updates version in build.gradle.kts and appends a changelog entry."
    group = "Versioning"

    // Define input properties for the task
    val version: String by project
    val changelogText: String by project

    doLast {
        // Parse the current version from build.gradle.kts
        val buildFile = file("agentseek/build.gradle.kts")
        val versionPattern = Regex("""version = "(\d+\.\d+\.\d+)"""")
        val currentVersionMatch = versionPattern.find(buildFile.readText())
        val currentVersion = currentVersionMatch?.groups?.get(1)?.value ?: throw IllegalArgumentException("Version not found in build.gradle.kts")

        // Update the version in build.gradle.kts
        buildFile.writeText(buildFile.readText().replace("version = \"$currentVersion\"", "version = \"$version\""))
        println("Version updated to $version in build.gradle.kts")

        // Update the CHANGELOG.md
        val changelogFile = file("CHANGELOG.md")
        val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val newChangelogEntry = """
            |# [$version] - $currentDate
            |${changelogText.trimIndent()}
        """.trimMargin()

        val existingChangelog = if (changelogFile.exists()) changelogFile.readText() else ""
        val updatedChangelog = "\n$newChangelogEntry\n\n$existingChangelog"
        changelogFile.writeText(updatedChangelog)
        println("Changelog updated with version $version")
    }
}