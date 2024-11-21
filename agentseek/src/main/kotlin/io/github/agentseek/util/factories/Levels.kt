package io.github.agentseek.util.factories

import io.github.agentseek.core.Scene
import io.github.agentseek.env.AgentSeekEnvironment
import io.github.agentseek.util.FastEntities.bounds
import io.github.agentseek.util.factories.GameObjects.cameraAgent
import io.github.agentseek.util.factories.GameObjects.guardAgent
import io.github.agentseek.util.factories.GameObjects.player
import io.github.agentseek.util.grid.Grid.Companion.useGrid
import io.github.agentseek.util.jason.JasonScenes.agents
import io.github.agentseek.util.jason.JasonScenes.sceneWithJason
import io.github.agentseek.view.gui.GameGui

object Levels {

    @JvmStatic
    fun main(args: Array<String>) {
        GameGui.startGameGui(scene = prisonLevel())
    }

    fun prisonLevel(): Scene = useGrid(rows = 7, columns = 10, 2.5) { grid ->
        sceneWithJason(
            name = "prisonScene", environmentClass = AgentSeekEnvironment::class,
            agents(
                cameraAgent(
                    id = "camera1",
                    position = grid(6, 9),
                ), guardAgent(
                    id = "agent1",
                    position = grid(1, 1),
                )
            ),
            player(
                position = grid(0, 0)
            ),
            *bounds(
                grid.boundsSize,
                GameGui.defaultRenderer(),
                GameGui.camera.viewPortWidth,
                GameGui.camera.viewPortHeight
            ),
        )
    }
}