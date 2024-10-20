package io.github.agentseek.view

import io.github.agentseek.common.Point2d

class Camera(
    private val view: View,
    private val worldViewPortWidth: Double = 50.0,
    private var cameraWorldPosition: Point2d = Point2d.origin(),
) {
    private val worldViewPortHeight
        get() = (worldViewPortWidth * view.screenHeight) / view.screenWidth

    /**
     * Converts a position in world's coordinates into a new position in screen's coordinates (i.e. camera coordinates)
     */
    fun toCameraPoint(worldPoint: Point2d): Point2d {
        val cameraX = ((worldPoint.x - cameraWorldPosition.x) * view.screenWidth) / worldViewPortWidth
        val cameraY = ((worldPoint.y - cameraWorldPosition.y) * view.screenHeight) / worldViewPortHeight
        return Point2d(cameraX, cameraY)
    }
}