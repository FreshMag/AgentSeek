package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.view.Camera
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(FieldMovementComponent::class)
class TestMouseComponent(gameObject: GameObject) : AbstractComponent(gameObject) {

    private lateinit var movement: FieldMovementComponent
    private val camera: Camera
        get() = GameEngine.view?.camera!!


    override fun init() {
        movement = gameObject.getComponent<FieldMovementComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        val mouse = Input.mouseClicked()
        if (mouse != null) {
            val worldPoint = camera.toWorldPoint(mouse)
            //movement.objective = worldPoint
            //VFX.expandingCircle(worldPoint, Color.BLACK, 1)
            VFX.fadingText(worldPoint, "!", Color.BLACK, 40, 500)
        }
    }
}