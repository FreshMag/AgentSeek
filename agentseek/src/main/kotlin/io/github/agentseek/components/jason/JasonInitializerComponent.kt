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

    override fun init() {
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
            agents: List<Agent>
        ): File {
            val content = """
        MAS $name {
        
            infrastructure: Centralised
            environment: ${environment.name}
            agents:
            ${agents.joinToString(separator = "\n\t\t") { "${it.id} ${it.aslAgentName};" }}
        
            aslSourcePath:
            "agentseek/src/main/asl";
        }
        """.trimIndent()

            val tempFile = File.createTempFile("MAS_", ".mas2j")
            tempFile.writeText(content)

            return tempFile
        }
    }
}