package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

internal class GameObjectSerializer : JsonSerializer<GameObject>() {
    private fun isFirstParameterAGameObject(componentClass: KClass<out Component>): Boolean =
        componentClass
            .primaryConstructor
            ?.parameters
            ?.firstOrNull()
            ?.type
            ?.classifier == (GameObject::class)

    private fun writeObjectArguments(objectWithArguments: Any, generator: JsonGenerator) {
        val componentClass = objectWithArguments::class
        generator.writeFieldName("arguments")
        generator.writeStartArray()
        val constructor = componentClass.primaryConstructor
        constructor?.parameters?.drop(1)?.forEach { par ->
            generator.writeStartObject()
            generator.writeStringField("name", par.name)
            generator.writeStringField("type", par.type.toString())
            val value = try {
                componentClass
                    .members
                    .firstOrNull { it.name == par.name }
                    ?.apply { isAccessible = true }
                    ?.call(objectWithArguments)
            } catch (e: Exception) {
                if (par.isOptional) {
                    constructor.callBy(mapOf())
                } else {
                    ""
                }
            }
            generator.writeObjectField("value", value)
            generator.writeEndObject()
        }
        generator.writeEndArray()
    }


    override fun serialize(gameObject: GameObject, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()

        generator.writeStringField("name", gameObject.name)

        // Rigid body
        generator.writeFieldName("rigidBody")
        generator.writeStartObject()
        generator.writeFieldName("shape")
        generator.writeStartObject()
        generator.writeObjectField("type", gameObject.rigidBody.shape::class.qualifiedName!!)
        generator.writeObjectField("params", gameObject.rigidBody.shape)
        generator.writeEndObject()
        generator.writeObjectField("mass", gameObject.rigidBody.mass)
        generator.writeObjectField("velocity", gameObject.rigidBody.velocity)
        generator.writeObjectField("static", gameObject.rigidBody.isStatic)
        generator.writeEndObject()

        // Components
        generator.writeFieldName("components")
        generator.writeStartArray()
        gameObject.components.forEach {
            generator.writeStartObject()

            val componentClass = it::class
            generator.writeStringField("class", componentClass.qualifiedName)

            check(isFirstParameterAGameObject(componentClass))

            writeObjectArguments(it, generator)
            generator.writeEndObject()
        }
        generator.writeEndArray()

        // Renderer
        generator.writeFieldName("renderer")
        generator.writeStartObject()
        generator.writeStringField("class", gameObject.renderer::class.qualifiedName!!)
        writeObjectArguments(gameObject.renderer, generator)
        generator.writeEndObject()

        generator.writeEndObject()

    }

}