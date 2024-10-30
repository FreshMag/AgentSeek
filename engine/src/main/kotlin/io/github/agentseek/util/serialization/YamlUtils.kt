package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.world.World
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files


private val deserializingModule = SimpleModule("DeserializingModule")

private val mapper: ObjectMapper
    get() {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule().apply {
            val module = SimpleModule()
                .addSerializer(GameObject::class.java, GameObjectSerializer())
                .addSerializer(Scene::class.java, SceneSerializer())
                .addDeserializer(Scene::class.java, SceneDeserializer())
            registerModule(module)
            registerModule(deserializingModule)
            configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
        }
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        return mapper
    }

internal object YAMLParse {
    /**
     * Takes in a data class (with ::class) and parses it by the fileName provided, returning the appropriate class
     * originally provided with parsed data.
     */
    inline fun <reified T : Any> parseDto(fileName: String): T? {
        try {
            val file = Files.newBufferedReader(FileSystems.getDefault().getPath(fileName))
            val result = file.use { mapper.readValue<T>(it) }
            file.close()
            return result
        } catch (e: NoSuchFileException) {
            return null
        }
    }

    inline fun <reified T : Any> parseString(content: String): T? =
        try {
            mapper.readValue<T>(content)
        } catch (e: JsonParseException) {
            null
        }

    fun parseGameObject(world: World, fileName: String): GameObject? {
        deserializingModule.addDeserializer(GameObject::class.java, GameObjectDeserializer(world))
        return parseDto<GameObject>(fileName)
    }

    fun parseGameObjectFromString(world: World, content: String): GameObject? {
        deserializingModule.addDeserializer(GameObject::class.java, GameObjectDeserializer(world))
        return parseString<GameObject>(content)
    }

}

internal object YAMLWrite {
    fun writeDto(fileName: String, content: Any) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(File(fileName), content)
    }
}