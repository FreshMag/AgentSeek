package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.world.World
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.reflect.KClass


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
        }
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        return mapper
    }

internal object YAMLParse {
    /**
     * Takes in a data class (with ::class) and parses it by the fileName provided, returning the appropriate class
     * originally provided with parsed data.
     */
    fun <T : Any> parseDto(fileName: String, dto: KClass<T>): T? {
        try {
            val file = Files.newBufferedReader(FileSystems.getDefault().getPath(fileName))
            val result = file.use { mapper.readValue(it, dto.java) }
            file.close()
            return result
        } catch (e: NoSuchFileException) {
            return null
        }
    }

    fun parseGameObject(world: World, fileName: String): GameObject? {
        deserializingModule.addDeserializer(GameObject::class.java, GameObjectDeserializer(world))
        return parseDto(fileName, GameObject::class)
    }
}

internal object YAMLWrite {
    fun writeDto(fileName: String, content: Any) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(File(fileName), content)
    }
}