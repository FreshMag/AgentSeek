package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import java.nio.file.Paths

internal class SceneSerializer : JsonSerializer<Scene>() {
    override fun serialize(scene: Scene, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()
        generator.writeFieldName("gameObjects")
        generator.writeStartArray()
        val serializer = provider.findValueSerializer(GameObject::class.java)
        val serializedNames = mutableListOf<String>()
        scene.gameObjects.forEach { gameObject ->
            if (serializeGameObjectsWithName && gameObject.name.isNotBlank()) {
                val path =
                    if (!serializedNames.contains(gameObject.name)) {
                        serializedNames += gameObject.name
                        gameObject.save(gameObjectSerializePath)
                    } else {
                        Paths.get(gameObjectSerializePath, "${gameObject.name}.gameObject.yaml").toString()
                    }
                generator.writeStartObject()
                generator.writeStringField("path", path)
                generator.writeFieldName("position")
                jacksonObjectMapper().writeValue(generator, gameObject.position)
                generator.writeEndObject()
            } else {
                serializer.serialize(gameObject, generator, provider)
            }
        }
        generator.writeEndArray()
        generator.writeFieldName("flags")
        jacksonObjectMapper().writeValue(generator, scene.flags)
        generator.writeEndObject()
    }

    companion object {
        var serializeGameObjectsWithName: Boolean = false
        var gameObjectSerializePath: String = "./"
    }
}