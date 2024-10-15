package io.github.agentseek.controller.events

import ryleh.common.Point2d

/**
 * This class manages a BulletSpawn Event and implements Event interface.
 */
class BulletSpawnEvent(spawner: GameObject, position: Point2d, velocity: Vector2d) : Event {
    private val position: Point2d = position
    private val velocity: Vector2d = velocity

    private val target: GameObject = spawner

    /**
     * {@inheritDoc}
     */
    override fun handle(state: GameState) {
        state.addEntity(
            BasicFactory.getInstance().createBullet(
                state, this.position, this.velocity,
                target.type
            )
        )
    }
}
