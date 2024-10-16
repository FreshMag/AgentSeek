package io.github.agentseek.events

import io.github.agentseek.core.GameState
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * This class manages other Event instances and implements EventListener.
 */
class EventHandler(private val gameState: GameState) : EventListener {
    private val eventQueue: BlockingQueue<Event> = LinkedBlockingQueue()

    /**
     * This method is called once every game loop. It checks all events inside the
     * Event Queue and handles their behavior.
     */
    fun handleEvents() {
        eventQueue.forEach { it.handle(this.gameState) }
        eventQueue.clear()
    }

    override fun notifyEvent(e: Event) {
        eventQueue.offer(e)
    }
}
