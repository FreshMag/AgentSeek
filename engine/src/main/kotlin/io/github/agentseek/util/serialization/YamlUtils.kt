package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.reflect.KClass

private val mapper: ObjectMapper
    get() {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        return mapper
    }

object YAMLParse {
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
}

object YAMLWrite {
    fun writeDto(fileName: String, content: Any) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(File("$fileName.yaml"), content)
    }
}