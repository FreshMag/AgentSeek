package io.github.agentseek.util.factories

import io.github.agentseek.core.Scene
import io.github.agentseek.util.FastEntities.bounds
import io.github.agentseek.util.FastEntities.scene
import io.github.agentseek.util.factories.GameObjects.wall
import io.github.agentseek.util.grid.Grid.Companion.useGrid
import io.github.agentseek.view.gui.GameGui

object Levels {

    @JvmStatic
    fun main(args: Array<String>) {
        GameGui.startGameGui(scene = prisonLevel())
    }

    fun prisonLevel(): Scene = useGrid(6, 10, 2.5) { grid ->
        scene(
//                    agents(
//                cameraAgent(
//                    id = "camera1",
//                    position = grid(4, 10),
//                ), guardAgent(
//                    id = "agent1",
//                    position = grid(1, 1),
//                )
//            ),
//            player(
//                position = grid(0, 0)
//            ),
            *bounds(
                grid.boundsSize,
                GameGui.defaultRenderer(),
                GameGui.camera.viewPortWidth,
                GameGui.camera.viewPortHeight
            ),
            *(0..5).flatMap { row ->
                (0..9).map { column ->
                    wall(
                        position = grid(row, column),
                        width = grid.cellWidth - 0.1,
                        height = grid.cellHeight - 0.1
                    )
                }
            }.toTypedArray()

        )
    }
}