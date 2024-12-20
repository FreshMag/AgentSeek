package io.github.agentseek.core

import io.github.agentseek.events.Event
import io.github.agentseek.events.EventHandler
import io.github.agentseek.events.EventListener
import io.github.agentseek.world.World
import io.github.agentseek.world.WorldImpl
import kotlin.time.Duration

class SceneImpl : Scene, EventListener {
    override val world: World = WorldImpl(this)
    override var isVictory: Boolean = false
    override var isGameOver: Boolean = false
    private val eventHandler: EventHandler = EventHandler(this)
    override val flags: MutableMap<String, Any> = mutableMapOf()

    override fun updateState(deltaTime: Duration) {
        world.gameObjects.forEach {
            it.onUpdate(deltaTime)
        }
        eventHandler.handleEvents()
    }

    override fun callGameOver(victory: Boolean) {
        isGameOver = true
        isVictory = victory
    }

    override fun notifyEvent(e: Event) {
        eventHandler.notifyEvent(e)
    }
}