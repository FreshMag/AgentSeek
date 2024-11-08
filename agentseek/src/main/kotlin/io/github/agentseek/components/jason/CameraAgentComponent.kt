package io.github.agentseek.components.jason

import io.github.agentseek.components.Requires
import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.FastEntities.radians
import jason.asSyntax.Literal
import jason.asSyntax.Structure

class CameraAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private var seesPlayer = false

    override fun init() {
        sightSensorComponent = SightSensorComponent(gameObject, 10.0, radians(30))
        gameObject.addComponent(sightSensorComponent)
        sightSensorComponent.init()
        sightSensorComponent.addReaction {
            seesPlayer = it.any { it.gameObject.name == "Player" }
        }
    }

    override fun execute(action: Structure) {}

    override fun getPercepts(): MutableList<Literal> {
        val seesPlayerLiteralValue = synchronized(seesPlayer) {
            if (seesPlayer) "yes" else "no"
        }
        return mutableListOf(
            Literal.parseLiteral("seesPlayer($seesPlayerLiteralValue)")
        )
    }
}