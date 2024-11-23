package io.github.agentseek.util.factories

import io.github.agentseek.core.Scene
import io.github.agentseek.env.AgentSeekEnvironment
import io.github.agentseek.util.FastEntities.bounds
import io.github.agentseek.util.factories.GameObjects.cameraAgent
import io.github.agentseek.util.factories.GameObjects.door
import io.github.agentseek.util.factories.GameObjects.guardAgent
import io.github.agentseek.util.factories.GameObjects.player
import io.github.agentseek.util.factories.GameObjects.wall
import io.github.agentseek.util.factories.GameObjects.walls
import io.github.agentseek.util.grid.Grid.Companion.useGrid
import io.github.agentseek.util.jason.JasonScenes.agents
import io.github.agentseek.util.jason.JasonScenes.sceneWithJason
import io.github.agentseek.view.gui.GameGui

object Levels {

    @JvmStatic
    fun main(args: Array<String>) {
        GameGui.startGameGui(scene = prisonLevel())
    }

    fun prisonLevel(): Scene = useGrid(rows = 5, columns = 7, 6.5) { grid ->
        sceneWithJason(
            name = "prisonScene", environmentClass = AgentSeekEnvironment::class,
            agents(
                cameraAgent(
                    id = "camera1",
                    position = grid(0.65, 6),
                    isCenter = false
                ), guardAgent(
                    id = "agent1",
                    position = grid(1, 0),
                )
            ),
            player(
                position = grid(2.5, 2.5)
            ),
            *bounds(
                grid.boundsSize,
                GameGui.defaultRenderer(),
                GameGui.camera.viewPortWidth,
                GameGui.camera.viewPortHeight
            ),
            *walls(
                wall(
                    position = grid(1, 2.25),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight / 2,
                    isCenter = false
                ),
                wall(
                    position = grid(1, 3.26),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight / 2,
                    isCenter = false
                ),
                wall(
                    position = grid(2, 2),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight * 2,
                ),
                wall(
                    position = grid(2, 4),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight * 2
                ),
                wall(
                    position = grid(3, 3),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight / 2
                ),
                wall(
                    position = grid(0, 2),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight * 2 + 1
                ),
                wall(
                    position = grid(1, 5),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight / 2,
                    isCenter = false
                ),
                wall(
                    position = grid(4, 6),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight * 2,
                ),
                wall(
                    position = grid(3.5, 0.75),
                    width = grid.cellWidth,
                    height = grid.cellHeight / 2,
                )
            ),
            door(
                destinationSceneName = "prisonScene",
                position = grid(-1.30, 0.5),
                size = grid.boundsSize
            )
        )
    }
}