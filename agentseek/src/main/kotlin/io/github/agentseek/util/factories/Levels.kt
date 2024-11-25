package io.github.agentseek.util.factories

import io.github.agentseek.components.ClickToExitComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.env.AgentSeekEnvironment
import io.github.agentseek.util.FastEntities.bounds
import io.github.agentseek.util.FastEntities.gameObject
import io.github.agentseek.util.FastEntities.scene
import io.github.agentseek.util.factories.GameObjects.cameraAgent
import io.github.agentseek.util.factories.GameObjects.door
import io.github.agentseek.util.factories.GameObjects.guardAgent
import io.github.agentseek.util.factories.GameObjects.hearingAgent
import io.github.agentseek.util.factories.GameObjects.player
import io.github.agentseek.util.factories.GameObjects.wall
import io.github.agentseek.util.factories.GameObjects.walls
import io.github.agentseek.util.grid.Grid.Companion.useGrid
import io.github.agentseek.util.jason.JasonScenes.agents
import io.github.agentseek.util.jason.JasonScenes.sceneWithJason
import io.github.agentseek.view.WhiteSceneWithCenteredTextRenderer
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
                    position = grid(0.65, 6.35),
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
                destinationSceneName = "Victory",
                position = grid(-1.30, 0.5),
                size = grid.boundsSize
            )
        )
    }

    fun obstacleLevel(): Scene = useGrid(rows = 8, columns = 10, 6.5) { grid ->
        sceneWithJason(
            name = "obstacleScene", environmentClass = AgentSeekEnvironment::class,
            agents(
                cameraAgent(
                    id = "camera1",
                    position = grid(0, 0),
                    isCenter = false
                ),
                cameraAgent(
                    id = "camera2",
                    position = grid(7, 9),
                    isCenter = false
                ),
                guardAgent(
                    id = "guard1",
                    position = grid(0, 9),
                ),
                guardAgent(
                    id = "guard2",
                    position = grid(6, 9),
                ),
                hearingAgent(
                    id = "hearing1",
                    position = grid(4, 5),
                )
            ),
            player(
                position = grid(7, 0)
            ),
            *bounds(
                grid.boundsSize,
                GameGui.defaultRenderer(),
                GameGui.camera.viewPortWidth,
                GameGui.camera.viewPortHeight
            ),
            *walls(
                wall(
                    position = grid(1, 1),
                    width = grid.cellWidth,
                    height = grid.cellHeight * 3,
                    isCenter = false
                ),
                wall(
                    position = grid(3, 2),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight / 2,
                ),
                wall(
                    position = grid(2, 5.49),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight / 2
                ),
                wall(
                    position = grid(4, 7),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight
                ),
                wall(
                    position = grid(6, 3.5),
                    width = grid.cellWidth * 1.5,
                    height = grid.cellHeight / 2,
                    isCenter = false
                ),
                wall(
                    position = grid(0.5, 6),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight * 1.5,
                    isCenter = false
                ),
                wall(
                    position = grid(7.5, 5),
                    width = grid.cellWidth / 2,
                    height = grid.cellHeight * 3,
                ),
                wall(
                    position = grid(5.5, 1.5),
                    width = grid.cellWidth * 2,
                    height = grid.cellHeight / 2,
                ),

                ),
            door(
                destinationSceneName = "Victory",
                position = grid(-3, 7.8),
                size = grid.boundsSize,
                isCenter = false
            )
        )
    }

    fun gameOverScene(): Scene =
        scene(
            gameObject(
                { ClickToExitComponent(it) },
                renderer = WhiteSceneWithCenteredTextRenderer(),
            )
        )
}