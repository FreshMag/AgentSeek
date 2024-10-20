package io.github.agentseek.view

import io.github.agentseek.common.Point2d

object Utils {
    /**
     * Note: world's dimensions are in meters!
     */
    private const val WORLD_MAX_WIDTH: Double = 50.0

    /**
     * Converts a position in world's coordinates into a new position in screen's coordinates (i.e. camera coordinates)
     */
    fun View.toCameraPoint(position: Point2d): Point2d {

        val worldHeight = (WORLD_MAX_WIDTH * screenHeight) / screenWidth
        val cameraX = (position.x * screenWidth) / WORLD_MAX_WIDTH
        val cameraY = (position.y * screenHeight) / worldHeight
        return Point2d(cameraX, cameraY)
    }
}