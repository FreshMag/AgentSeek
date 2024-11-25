package io.github.agentseek.util.grid

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.Scene
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.vector
import io.github.agentseek.view.Camera
import io.github.agentseek.view.gui.GameGui

/**
 * A grid of cells that facilitates the positioning of entities in a scene.
 * @property rows the number of rows in the grid
 * @property columns the number of columns in the grid
 * @property camera the camera that the grid is relative to
 * @property boundsSize the size of the bounds around the grid
 */
data class Grid(val rows: Int, val columns: Int, val camera: Camera, val boundsSize: Number) {

    /**
     * Width of the playable area in the scene (the one contained within the bounds).
     */
    val playableWidth = camera.viewPortWidth - (boundsSize.toDouble() * 2) - (SAFETY_MARGIN * 2)

    /**
     * Height of the playable area in the scene (the one contained within the bounds).
     */
    val playableHeight = camera.viewPortHeight - (boundsSize.toDouble() * 2) - (SAFETY_MARGIN * 2)

    /**
     * The origin of the grid in the scene.
     */
    val arenaOrigin = camera.cameraWorldPosition + vector(
        boundsSize.toDouble() + SAFETY_MARGIN,
        boundsSize.toDouble() + SAFETY_MARGIN
    )
    /**
     * The width of a cell in the grid.
     */
    val cellWidth = playableWidth / columns
    /**
     * The height of a cell in the grid.
     */
    val cellHeight = playableHeight / rows

    /**
     * Returns the position of the center of the cell at the specified [row] and [column]. If [center] is true, the
     * position is the center of the cell, otherwise it is the top-left corner.
     */
    operator fun invoke(row: Number, column: Number, center: Boolean = true): Point2d {
        val x = arenaOrigin.x + (column.toDouble() + if (center) 0.5 else 0.0) * cellWidth
        val y = arenaOrigin.y + (row.toDouble() + if (center) 0.5 else 0.0) * cellHeight
        return point(x, y)
    }

    companion object {
        /**
         * The safety margin around the grid, to avoid objects clipping with the bounds.
         */
        private const val SAFETY_MARGIN = 0.25

        /**
         * Creates a grid with the specified number of [rows] and [columns], and the specified [boundsSize], using
         * the [GameGui] camera.
         */
        fun make(rows: Int, columns: Int, boundsSize: Number): Grid =
            Grid(rows, columns, GameGui.camera, boundsSize)

        /**
         * Utility function to create a scene with a grid.
         */
        fun useGrid(rows: Int, columns: Int, boundsSize: Number, scene: (Grid) -> Scene): Scene =
            scene(make(rows, columns, boundsSize))

    }
}