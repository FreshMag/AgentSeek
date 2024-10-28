package io.github.agentseek.util.serialization

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.world.World
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


/**
 * Saves a [GameObject] into a YAML file. Returns the path to the saved file
 */
fun GameObject.save(directory: String, name: String = this.name): String {
    val objectName = name.ifBlank { UUID.randomUUID().toString() }
    val path = Paths.get(directory, "$objectName.gameObject.yaml")
    Files.createDirectories(path.parent)
    YAMLWrite.writeDto(path.toString(), this)
    return path.toString()
}

/**
 * Loads a [GameObject] of [name] from a YAML file in [directory].
 */
fun World.loadGameObject(directory: String, name: String): GameObject? =
    loadGameObject(Paths.get(directory, "$name.gameObject.yaml").toString())

/**
 * Loads a [GameObject] reading it from a YAML file at [path].
 */
fun World.loadGameObject(path: String): GameObject? = YAMLParse.parseGameObject(this, path)

/**
 * Saves a [Scene] into a YAML file. Returns the path to the saved file
 */
fun Scene.save(directory: String, name: String, serializeGameObjectsWithName: Boolean = true): String {
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
        YAMLParse.parseDto<Scene>(Paths.get(directory, "$name.scene.yaml").toString())

    /**
     * Loads a [Scene] taken from `"<RESOURCE_DIR>/yaml/scenes/<NAME>.scene.yaml"`
     */
    fun Any.loadSceneFromResource(name: String): Scene? =
        this::class.java.getResource("/yaml/scenes/$name.scene.yaml")
            ?.also { SceneDeserializer.resourcePath = this::class.java.getResource("/yaml/gameObjects/")?.path ?: "" }
            ?.let {
                YAMLParse.parseString<Scene>(
                    it.readText()
                )
            }
}
