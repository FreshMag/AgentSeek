package io.github.agentseek.components.jason

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import jason.infra.local.RunLocalMAS
import jason.runtime.RuntimeServicesFactory
import java.io.File

/**
 * Utility data class for loading a Jason agent.
 */
data class Agent(val id: String, val aslAgentName: String)

/**
 * Component that initializes a Jason environment.
 */
class JasonInitializerComponent(
    gameObject: GameObject,
    private val mas2jName: String,
    private val environmentClassFQName: String,
    private val agents: List<Agent>,
    private val hideJasonGui: Boolean = false,
) : AbstractComponent(gameObject) {

    private var tempFiles: List<File> = emptyList()

    override fun init() {
        tempFiles = generateTemporaryAgentFiles(agents)
        val file = generateTempFile(mas2jName, Class.forName(environmentClassFQName), agents).absolutePath
        jasonManager = this.gameObject
        Thread {
            RunLocalMAS.main(arrayOf(file))
        }.start()
    }

    override fun onRemoved() {
        // stops the MAS
        val rs = RuntimeServicesFactory.get()
        rs.stopMAS(0, false, 0)
        tempFiles.forEach { it.delete() }
    }

    companion object {
        /**
         * Reference to the Jason manager object. This is used to send messages to the Jason environment.
         */
        var jasonManager: GameObject? = null

        /**
         * Generates a temporary MAS file used to launch the Jason environment.
         */
        private fun generateTempFile(
            name: String,
            environment: Class<*>,
            agents: List<Agent>,
        ): File {
            val content = """
        MAS $name {
        
            infrastructure: Centralised
            environment: ${environment.name}
            agents:
            ${agents.joinToString(separator = "\n\t\t") { "${it.id} ${it.aslAgentName};" }}
        
            aslSourcePath:
            ".";
        }
        """.trimIndent()

            val tempFile = File.createTempFile("MAS_", ".mas2j")
            tempFile.writeText(content)

            return tempFile
        }

        /**
         * Generates temporary agent files used inside the Jason environment. Returns the list of files created
         * (they must be manually deleted).
         */
        private fun generateTemporaryAgentFiles(agents: List<Agent>): List<File> =
            agents.map { agent ->
                val resource = object {}.javaClass.classLoader.getResourceAsStream(agent.aslAgentName + ".asl")
                if (resource == null) {
                    throw IllegalStateException("Internal exception: Resource for agent ${agent.aslAgentName} not found")
                }
                resource.bufferedReader().use {
                    val file = File("${agent.aslAgentName}.asl")
                    file.createNewFile()
                    file.writeText(it.readText())
                    file
                }
            }

    }
}