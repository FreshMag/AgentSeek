package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene

internal class SceneSerializer : JsonSerializer<Scene>() {

    private fun writeGameObject(
        gameObject: GameObject,
        generator: JsonGenerator,
        serializedNames: MutableSet<String>,
        serializer: JsonSerializer<Any>,
        provider: SerializerProvider,
    ) {
        if (serializeGameObjectsWithName && gameObject.name.isNotBlank()) {
            if (!serializedNames.contains(gameObject.name)) {
                serializedNames += gameObject.name
                gameObject.save(gameObjectSerializePath)
            }
            generator.writeStartObject()
            generator.writeStringField("resource", gameObject.name)
            generator.writeFieldName("position")
            jacksonObjectMapper().writeValue(generator, gameObject.position)
            generator.writeEndObject()
        } else {
            serializer.serialize(gameObject, generator, provider)
        }
    }

    override fun serialize(scene: Scene, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()

        // GameObjects
        generator.writeFieldName("gameObjects")
        val serializer = provider.findValueSerializer(GameObject::class.java)
        val serializedNames = mutableSetOf<String>()
        generator.writeStartArray()
        scene.gameObjects.forEach { writeGameObject(it, generator, serializedNames, serializer, provider) }
        generator.writeEndArray()

        // Flags
        generator.writeFieldName("flags")
        jacksonObjectMapper().writeValue(generator, scene.flags)
        generator.writeEndObject()
    }

    companion object {
        var serializeGameObjectsWithName: Boolean = false
        var gameObjectSerializePath: String = "./"
    }
}