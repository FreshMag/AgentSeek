package ryleh.controller.events

import io.github.agentseek.components.HealthIntComponent
import io.github.agentseek.core.GameState
import io.github.agentseek.events.Event
import io.github.agentseek.events.EventListener
import java.util.function.Consumer

/**
 * This class manages other Event instances and implements EventListener.
 */
class EventHandler(private val gameState: GameState) : EventListener {
    private val eventQueue: MutableList<Event> = ArrayList()
    private lateinit var comp: HealthIntComponent

    /**
     * This method is called once every game loop. It checks all events inside the
     * Event Queue and handles their behavior.
     */
    fun checkEvents() {
        if (eventQueue.isNotEmpty()) {
            eventQueue.forEach(Consumer { e ->
                e.handle(this.gameState)
            })
            eventQueue.clear()
        }
    }


    override fun notifyEvent(e: Event) {
        eventQueue.add(e)
    }
}
