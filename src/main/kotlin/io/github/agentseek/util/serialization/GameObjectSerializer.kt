package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.agentseek.core.GameObject

class GameObjectSerializer : JsonSerializer<GameObject>() {
    override fun serialize(gameObject: GameObject?, generator: JsonGenerator?, provider: SerializerProvider?) {
//        val gen = generator ?: return
//        val go = gameObject ?: return
//        gen.writeStartObject()
//        gen.writeStringField("id", go.id)
    }

}