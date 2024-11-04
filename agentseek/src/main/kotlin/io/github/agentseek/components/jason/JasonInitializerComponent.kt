package io.github.agentseek.components.jason

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import jason.infra.local.RunLocalMAS
import java.io.File

class JasonInitializerComponent(
    gameObject: GameObject,
    private val mas2jName: String,
    private val environmentClassFQName: String,
    private val agents: List<Pair<String, String>>,
) : AbstractComponent(gameObject) {

    override fun init() {
        val file = generateTempFile(mas2jName, Class.forName(environmentClassFQName), agents).path
        jasonManager = this.gameObject
        Thread {
            RunLocalMAS.main(arrayOf(file))
        }.start()
    }

    companion object {
        var jasonManager: GameObject? = null


        private fun generateTempFile(
            name: String,
            environment: Class<*>,
            agents: List<Pair<String, String>>
        ): File {
            val content = """
        MAS $name {
        
            infrastructure: Centralised
            environment: ${environment.name}
            agents:
            ${agents.joinToString(separator = "\n\t\t") { "${it.first} ${it.second};" }}
        
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