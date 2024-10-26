package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.agentseek.core.GameObject

class GameObjectSerializer : JsonSerializer<GameObject>() {
    override fun serialize(gameObject: GameObject, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()
        generator.writeFieldName("components")
        generator.writeStartArray()
        generator.writeStartObject()
        gameObject.components.forEach {
            generator.writeStringField("class", it::class.qualifiedName)
        }
        generator.writeEndObject()
        generator.writeEndArray()
        generator.writeEndObject()

    }

}