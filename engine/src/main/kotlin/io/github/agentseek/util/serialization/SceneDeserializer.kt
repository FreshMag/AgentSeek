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
import java.nio.file.Paths

internal class SceneDeserializer : JsonDeserializer<Scene>() {

    private fun Scene.addGameObjectFromNode(
        gameObjectNode: JsonNode,
        deserializer: GameObjectDeserializer,
        parser: JsonParser,
        context: DeserializationContext
    ) {
        val go = if (gameObjectNode.has("resource")) {
            // read GameObject from resource
            val path = Paths.get(
                resourcePath,
                "${gameObjectNode["resource"].asText()}.gameObject.yaml"
            ).toString()
            this.world.loadGameObject(path)?.apply {
                if (gameObjectNode.has("position")) {
                    val position =
                        jacksonObjectMapper().treeToValue(gameObjectNode["position"], Point2d::class.java)
                    this.position = position
                }
            }
        } else {
            // read serialized GameObject
            val gameObjectParser = gameObjectNode.traverse(parser.codec)
            deserializer.deserialize(gameObjectParser, context)
        } ?: return
        this.world.addGameObject(go)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Scene {
        val node: JsonNode = parser.codec.readTree(parser)
        val scene = emptyScene()

        // Flags
        val flags = jacksonObjectMapper().treeToValue(node["flags"], object : TypeReference<Map<String, Any>>() {})
        scene.flags += flags

        // GameObjects
        val gameObjectDeserializer = GameObjectDeserializer(scene.world)
        node["gameObjects"].forEach { scene.addGameObjectFromNode(it, gameObjectDeserializer, parser, context) }

        return scene
    }

    companion object {
        var resourcePath: String = SceneDeserializer::class.java.classLoader.getResource("")?.path ?: ""
    }
}