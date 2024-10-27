package io.github.agentseek.util.serialization

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.world.World
import java.nio.file.Paths
import java.util.*

/**
 * Saves a [GameObject] into a YAML file. Returns the path to the saved file
 */
fun GameObject.save(directory: String, name: String = this.name): String {
    val objectName = name.ifBlank { UUID.randomUUID().toString() }
    val path = Paths.get(directory, "$objectName.gameObject.yaml").toString()
    YAMLWrite.writeDto(path, this)
    return path
}

/**
 * Loads a [GameObject] of [name] from a YAML file in [directory].
 */
fun World.loadGameObject(directory: String, name: String): GameObject? =
    YAMLParse.parseGameObject(this, Paths.get(directory, "$name.gameObject.yaml").toString())

/**
 * Saves a [Scene] into a YAML file. Returns the path to the saved file
 */
fun Scene.save(directory: String, name: String, serializeGameObjectsWithName: Boolean = false): String {
    SceneSerializer.serializeGameObjectsWithName = serializeGameObjectsWithName
    SceneSerializer.gameObjectSerializePath = directory
    val path = Paths.get(directory, "$name.scene.yaml").toString()
    YAMLWrite.writeDto(path, this)
    return path
}

object Scenes {
    /**
     * Loads a [Scene] from a YAML file.
     */
    fun load(directory: String, name: String): Scene? =
        YAMLParse.parseDto(Paths.get(directory, "$name.scene.yaml").toString(), Scene::class)
}
