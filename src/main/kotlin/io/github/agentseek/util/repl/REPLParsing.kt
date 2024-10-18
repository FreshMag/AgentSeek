package io.github.agentseek.util.repl

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
import io.github.agentseek.util.FastEntities.emptyGameObject
import io.github.agentseek.util.repl.GameREPL.isRunning
import io.github.agentseek.util.repl.GameREPL.scene
import io.github.agentseek.view.SimpleRenderer
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import kotlin.system.exitProcess

object REPLParsing {
    @Command(
        name = "GameREPL",
        mixinStandardHelpOptions = true,
        subcommands = [
            StartCommand::class,
            PauseCommand::class,
            ResumeCommand::class,
            ExitCommand::class,
            AddGO::class,
            InspectGO::class,
            DeleteGO::class],
        description = ["Game Read-Eval-Print-Loop for utility"],
        version = [
            "@|yellow AgentSeek-REPL |@",
            "@|blue Build 1.0|@",
            "@|red,bg(white) (c) 2024|@"]
    )
    class REPLCommand : Runnable {
        override fun run() {
            println("Type 'help' to see available commands")
        }
    }


    @Command(
        name = "start",
        description = ["Starts an infinite game loop"]
    )
    class StartCommand : Runnable {
        override fun run() {
            GameEngine.start()
            isRunning = true
        }
    }

    @Command(
        name = "pause",
        description = ["Pauses a running game loop"]
    )
    class PauseCommand : Runnable {
        override fun run() {
            GameEngine.pause()
            isRunning = false
        }
    }

    @Command(
        name = "resume",
        description = ["Resumes the game loop"]
    )
    class ResumeCommand : Runnable {
        override fun run() {
            GameEngine.resume()
            isRunning = true
        }
    }

    @Command(
        name = "exit",
        description = ["Exits the program"]
    )
    class ExitCommand : Runnable {
        override fun run() {
            GameEngine.stop()
            exitProcess(0)
        }
    }

    @Command(
        name = "addgo",
        description = ["Adds a game object"]
    )
    class AddGO : Runnable {
        private val go = scene.emptyGameObject(false)

        @Option(
            names = ["-f", "--form"],
            description = ["specify the form of the game object (e.g. circle(<RADIUS>), rectangle(<WIDTH>, <HEIGHT>))"],
            required = false,
        )
        var form: String = "circle(${GameObject.DEFAULT_HITBOX_RADIUS})"

        @Option(
            names = ["x", "--x"],
            description = ["specify the x coordinate"],
        )
        var x: Double = 0.0

        @Option(
            names = ["y", "--y"],
            description = ["specify the y coordinate"],
        )
        var y: Double = 0.0

        @Option(
            names = ["-c", "--components"],
            split = ",",
            description = ["Fully qualified names of the Components to be added to this Game Object"],
            arity = "1..*"
        )
        var components: Array<String> = emptyArray()

        override fun run() {
            components.forEach {
                try {
                    val component: Component = ClassLoader.getSystemClassLoader()
                        .loadClass(it)
                        .getConstructor(GameObject::class.java)
                        .newInstance(go) as Component
                    go.addComponent(component)
                } catch (e: ClassNotFoundException) {
                    println("Component class $it not found")
                }
            }
            val hitBox = parseForm(form)
            go.hitBox = hitBox ?: return
            go.position = Point2d(x, y)
            go.renderer = SimpleRenderer()
            scene.world.addGameObject(go.also { println(it.id) })
        }
    }

    private fun parseForm(form: String): HitBox? {
        try {
            val split = form.split("(")
            val shape = split[0]
            val args = split[1].substringBefore(")").split(",")
            return when (shape) {
                "circle" -> CircleHitBox(args[0].toInt())
                else -> CircleHitBox(GameObject.DEFAULT_HITBOX_RADIUS)
            }
        } catch (e: NumberFormatException) {
            println("Please provide an integer value between parentheses")
            return null
        } catch (e: Exception) {
            println("$form is not a valid form expression. Try circle(<RADIUS>) or rectangle(<WIDTH>, <HEIGHT>)")
            return null
        }
    }

    @Command(
        name = "delgo",
        description = ["Deletes a gameobject"]
    )
    class DeleteGO : Runnable {
        @Option(
            names = ["-i", "--id"],
            description = ["ID of the game object to delete"],
            required = true
        )
        lateinit var id: String
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                scene.world.removeGameObject(it)
            } ?: println("No GameObject was deleted: ID didn't match any GameObject")
        }
    }
    @Command(
        name = "inspect",
        description = ["Inspects a game object"]
    )
    class InspectGO: Runnable {
        @Option(
            names = ["-i", "--id"],
            description = ["ID of the game object to inspect"],
            required = true
        )
        lateinit var id: String
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                println(it.toString())
            } ?: println("ID doesn't match any GameObject")
        }
    }
}