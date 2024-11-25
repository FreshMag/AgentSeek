package io.github.agentseek.util.factories

import io.github.agentseek.components.ClickToExitComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.env.AgentSeekEnvironment
import io.github.agentseek.util.FastEntities.bounds
import io.github.agentseek.util.FastEntities.gameObject
import io.github.agentseek.util.FastEntities.point
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
        GameGui.startGameGui(scene = pathLevel())
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

    fun randomLevel(): Scene = useGrid(rows = 10, columns = 10, 5) { grid ->
        sceneWithJason(
            name = "easy", environmentClass = AgentSeekEnvironment::class,
            agents(
                cameraAgent(
                    id = "camera0",
                    position = grid(0, 5)
                ),
                cameraAgent(
                    id = "camera1",
                    position = grid(2, 0)
                ),
                cameraAgent(
                    id = "camera2",
                    position = grid(5, 0)
                ),
                cameraAgent(
                    id = "camera3",
                    position = grid(9, 5)
                ),
                cameraAgent(
                    id = "camera4",
                    position = grid(9, 7)
                ),
                cameraAgent(
                    id = "camera5",
                    position = grid(9, 9)
                ),
                cameraAgent(
                    id = "camera6",
                    position = grid(0, 5)
                ),
                cameraAgent(
                    id = "camera7",
                    position = grid(0, 9)
                ),
                cameraAgent(
                    id = "camera8",
                    position = grid(0, 3)
                ),
                guardAgent(
                    id = "agent1",
                    position = grid(2, 2),
                ),
            ),
            player(
                position = grid(5, 5)
            ),
            *bounds(
                grid.boundsSize,
                GameGui.defaultRenderer(),
                GameGui.camera.viewPortWidth,
                GameGui.camera.viewPortHeight
            ),
            *walls(
                wall(
                    position = grid(3, 3),
                    width = grid.cellWidth,
                    height = grid.cellHeight,
                ),
                wall(
                    position = grid(3, 5),
                    width = grid.cellWidth,
                    height = grid.cellHeight,
                ),
                wall(
                    position = grid(3, 7),
                    width = grid.cellWidth,
                    height = grid.cellHeight,
                ),
                wall(
                    position = grid(5, 3),
                    width = grid.cellWidth,
                    height = grid.cellHeight,
                ),
                wall(
                    position = grid(5, 7),
                    width = grid.cellWidth,
                    height = grid.cellHeight
                ),
                wall(
                    position = grid(7, 3),
                    width = grid.cellWidth,
                    height = grid.cellHeight,
                ),
                wall(
                    position = grid(7, 5),
                    width = grid.cellWidth,
                    height = grid.cellHeight
                ),
                wall(
                    position = grid(7, 7),
                    width = grid.cellWidth,
                    height = grid.cellHeight
                ),
            ),
            door(
                destinationSceneName = "prisonScene",
                position = grid(-1.30, 0.5),
                size = grid.boundsSize
            )
        )
    }

    fun pathLevel(): Scene = sceneWithJason(
        name = "example", environmentClass = AgentSeekEnvironment::class,
        agents = agents(
            cameraAgent(
                id = "camera1",
                position = point(GameGui.camera.viewPortWidth - 4, GameGui.camera.viewPortHeight - 10),
            ), guardAgent(
                id = "agent1",
                position = point(8, 4),
            ),
            guardAgent(
                id = "agent2",
                position = point(35, 5),
            )
        ),
        player(
            position = point(GameGui.camera.viewPortWidth - 5, GameGui.camera.viewPortHeight - 5.5)
        ),
        *bounds(2.5, GameGui.defaultRenderer(), GameGui.camera.viewPortWidth, GameGui.camera.viewPortHeight),
        *walls(
            wall(
                x = GameGui.camera.viewPortWidth - 10,
                y = GameGui.camera.viewPortHeight - 8,
                width = 15,
                height = 1,
            ),
            wall(
                x = 35,
                y = 7,
                width = 10,
                height = 1,
            ),
            wall(
                x = 20,
                y = 13,
                width = 20,
                height = 1,
            ),
            wall(
                x = 10,
                y = 23,
                width = 35,
                height = 1,
            ),
            wall(
                x = 10,
                y = 10,
                width = 1,
                height = 16,
            ),
            wall(
                x = 15,
                y = 20,
                width = 1,
                height = 5,
            ),
            wall(
                x = 23,
                y = 20,
                width = 1,
                height = 5,
            ),
            wall(
                x = 35,
                y = 18,
                width = 1,
                height = 10,
            ),
            wall(
                x = 40,
                y = 19,
                width = 1,
                height = 5,
            ),
            wall(
                x = 31,
                y = 19,
                width = 7,
                height = 1,
            ),
            wall(
                x = 28,
                y = 26,
                width = 1,
                height = 14,
            )
        ),
        door(
            "jasonExample",
            position = point(0, 10)
        )
    )

    fun gameOverScene(): Scene =
        scene(
            gameObject(
                { ClickToExitComponent(it) },
                renderer = WhiteSceneWithCenteredTextRenderer(),
            )
        )
}