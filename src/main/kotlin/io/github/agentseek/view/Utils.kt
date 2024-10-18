package io.github.agentseek.view

import io.github.agentseek.common.Point2d

object Utils {
    private const val WORLD_MAX_WIDTH: Double = 1000.0

    fun GameGui.toCameraPoint(position: Point2d): Point2d {
        val width = screenSize.width
        val height = screenSize.height

        val worldHeight = (WORLD_MAX_WIDTH * height) / width
        val cameraX = (position.x * width) / WORLD_MAX_WIDTH
        val cameraY = (position.y * height) / worldHeight
        return Point2d(cameraX, cameraY)
    }
}