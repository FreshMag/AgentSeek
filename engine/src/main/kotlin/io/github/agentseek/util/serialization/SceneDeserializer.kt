package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.agentseek.common.Point2d
import io.github.agentseek.core.Scene
import io.github.agentseek.util.factories.SceneFactory.emptyScene

internal class SceneDeserializer : JsonDeserializer<Scene>() {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Scene {
        val node: JsonNode = parser.codec.readTree(parser)
        val scene = emptyScene()

        // Flags
        val flags = jacksonObjectMapper().treeToValue(node["flags"], object : TypeReference<Map<String, Any>>() {})
        scene.flags += flags

        // GameObjects
        val gameObjectDeserializer = GameObjectDeserializer(scene.world)
        node["gameObjects"].forEach { gameObjectNode ->
            val go = if (gameObjectNode.has("path")) {
                // read GameObject from path
                scene.world.loadGameObject(gameObjectNode["path"].asText())?.apply {
                    if (gameObjectNode.has("position")) {
                        val position =
                            jacksonObjectMapper().treeToValue(gameObjectNode["position"], Point2d::class.java)
                        this.position = position
                    }
                }
            } else {
                // read serialized GameObject
                val gameObjectParser = gameObjectNode.traverse(parser.codec)
                gameObjectDeserializer.deserialize(gameObjectParser, context)
            } ?: return@forEach
            scene.world.addGameObject(go)
        }

        return scene
    }
}