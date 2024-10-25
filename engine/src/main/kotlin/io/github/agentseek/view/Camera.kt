package io.github.agentseek.view

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Rectangle2d

/**
 * Represents the camera that performs transformations from world's coordinate space into a screen coordinate space.
 */
class Camera(
    private val view: View,
    /**
     * Maximum portion of the world that can be seen within this camera.
     */
    worldViewPortWidth: Double = 50.0,
    /**
     * Position of the camera (in world's coordinates).
     */
    private var cameraWorldPosition: Point2d = Point2d.origin(),
) {
    var viewPortWidth = worldViewPortWidth
        private set
    val viewPortHeight
        get() = (viewPortWidth * view.screenHeight) / view.screenWidth

    /**
     * Converts a position in world's coordinates into a new position in screen's coordinates (i.e. camera coordinates)
     */
    fun toCameraPoint(worldPoint: Point2d): Point2d {
        val cameraX = ((worldPoint.x - cameraWorldPosition.x) * view.screenWidth) / viewPortWidth
        val cameraY = ((worldPoint.y - cameraWorldPosition.y) * view.screenHeight) / viewPortHeight
        return Point2d(cameraX, cameraY)
    }

    /**
     * Converts a position in camera's coordinates (screen) into a new position in world's coordinates
     */
    fun toWorldPoint(cameraPoint: Point2d): Point2d {
        val worldX = (cameraPoint.x * viewPortWidth) / view.screenWidth + cameraWorldPosition.x
        val worldY = (cameraPoint.y * viewPortHeight) / view.screenHeight + cameraWorldPosition.y
        return Point2d(worldX, worldY)
    }

    /**
     * Converts a length in world terms into camera terms (by a simple proportion)
     */
    fun toCameraLength(worldLength: Double): Double =
        (worldLength / viewPortWidth) * view.screenWidth

    /**
     * Converts a [Circle2d] in world terms into camera terms
     */
    fun toCameraCircle(circle2d: Circle2d): Circle2d =
        Circle2d(toCameraLength(circle2d.radius), toCameraPoint(circle2d.position))

    /**
     * Converts a [Rectangle2d] in world terms into camera terms
     */
    fun toCameraRectangle(rectangle2d: Rectangle2d): Rectangle2d =
        Rectangle2d(
            toCameraPoint(rectangle2d.upperLeft),
            toCameraLength(rectangle2d.width),
            toCameraLength(rectangle2d.height),
        )

    /**
     * Converts a [Cone2d] in world terms into camera terms
     */
    fun toCameraCone(cone2d: Cone2d): Cone2d =
        Cone2d(
            toCameraPoint(cone2d.vertex),
            cone2d.angle,
            toCameraLength(cone2d.length),
            cone2d.rotation
        )

    /**
     * Zooms out or in the camera by a [factor]
     */
    fun zoom(factor: Double) {
        viewPortWidth /= factor
    }
}