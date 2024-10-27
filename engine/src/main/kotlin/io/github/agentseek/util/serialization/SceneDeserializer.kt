package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import io.github.agentseek.core.Scene

internal class SceneDeserializer : JsonDeserializer<Scene>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Scene {
        TODO("Not yet implemented")
    }
}