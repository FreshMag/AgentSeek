package io.github.agentseek.util.serialization

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.world.World
import java.nio.file.Paths

/**
 * Saves a [GameObject] into a YAML file.
 */
fun GameObject.save(directory: String, name: String) {
    YAMLWrite.writeDto(Paths.get(directory, "$name.gameObject.yaml").toString(), this)
}

/**
 * Loads a [GameObject] of [name] from a YAML file in [directory].
 */
fun World.loadGameObject(directory: String, name: String): GameObject? =
    YAMLParse.parseGameObject(this, Paths.get(directory, "$name.gameObject.yaml").toString())

/**
 * Saves a [Scene] into a YAML file.
 */
fun Scene.save(directory: String, name: String) {
    YAMLWrite.writeDto(Paths.get(directory, "$name.scene.yaml").toString(), this)
}

object Scenes {
    /**
     * Loads a [Scene] from a YAML file.
     */
    fun load(directory: String, name: String): Scene? =
        YAMLParse.parseDto(Paths.get(directory, "$name.scene.yaml").toString(), Scene::class)
}
