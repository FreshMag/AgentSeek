package io.github.agentseek.view

import java.util.*

class RenderingContext<T> {
    private val effects: MutableList<(T) -> Unit> = Collections.synchronizedList(mutableListOf())

    fun sinkToBuffer(): List<(T) -> Unit> = effects.toList()

    fun clearEvents() = effects.clear()

    fun render(effect: (T) -> Unit) = effects.add(effect)

}
