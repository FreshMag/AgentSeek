package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.agentseek.core.Scene

class SceneSerializer : JsonSerializer<Scene>() {
    override fun serialize(p0: Scene?, p1: JsonGenerator?, p2: SerializerProvider?) {
        TODO("Not yet implemented")
    }
}