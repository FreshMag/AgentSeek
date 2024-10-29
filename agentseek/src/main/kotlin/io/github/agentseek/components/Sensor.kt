package io.github.agentseek.components

/**
 * Represents a Sensor that perceive a [T] from the environment and performs reactions.
 */
interface Sensor<T> {
    /**
     * Adds a reaction to be executed when the sensor perceives data.
     */
    fun addReaction(reaction: (T) -> Unit)
}