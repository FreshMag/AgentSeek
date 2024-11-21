package io.github.agentseek.util.grid

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.Scene
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.vector
import io.github.agentseek.view.Camera
import io.github.agentseek.view.gui.GameGui

data class Grid(val rows: Int, val columns: Int, val camera: Camera, val boundsSize: Number) {

    val playableWidth = camera.viewPortWidth - (boundsSize.toDouble() * 2) - (SAFETY_MARGIN * 2)
    val playableHeight = camera.viewPortHeight - (boundsSize.toDouble() * 2) - (SAFETY_MARGIN * 2)
    val arenaOrigin = camera.cameraWorldPosition + vector(
        boundsSize.toDouble() + SAFETY_MARGIN,
        boundsSize.toDouble() + SAFETY_MARGIN
    )

    val cellWidth = playableWidth / columns
    val cellHeight = playableHeight / rows

    operator fun invoke(row: Int, column: Int, center: Boolean = false): Point2d {
        val x = arenaOrigin.x + (column.toDouble() + if (center) 0.5 else 0.0) * cellWidth
        val y = arenaOrigin.y + (row.toDouble() + if (center) 0.5 else 0.0) * cellHeight
        return point(x, y)
    }

    companion object {

        private const val SAFETY_MARGIN = 0.25

        fun make(rows: Int, columns: Int, boundsSize: Number): Grid =
            Grid(rows, columns, GameGui.camera, boundsSize)

        fun useGrid(rows: Int, columns: Int, boundsSize: Number, scene: (Grid) -> Scene): Scene =
            scene(make(rows, columns, boundsSize))

    }
}