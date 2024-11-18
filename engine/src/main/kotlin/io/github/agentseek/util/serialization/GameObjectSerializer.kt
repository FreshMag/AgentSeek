package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.RigidBody
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.javaType
import kotlin.reflect.jvm.isAccessible

internal class GameObjectSerializer : JsonSerializer<GameObject>() {

    override fun serialize(gameObject: GameObject, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()

        generator.writeStringField("name", gameObject.name)

        // Rigid body
        gameObject.writeRigidBody(generator)

        // Components
        gameObject.writeComponents(generator)

        // Renderer
        gameObject.writeRenderer(generator)

        generator.writeEndObject()

    }

    private fun isFirstParameterAGameObject(componentClass: KClass<out Component>): Boolean =
        componentClass
            .primaryConstructor
            ?.parameters
            ?.firstOrNull()
            ?.type
            ?.classifier == (GameObject::class)

    @OptIn(ExperimentalStdlibApi::class)
    private fun writeObjectArguments(objectWithArguments: Any, generator: JsonGenerator) {
        val componentClass = objectWithArguments::class
        generator.writeFieldName("arguments")
        generator.writeStartArray()
        val constructor = componentClass.primaryConstructor
        constructor?.parameters?.drop(1)?.forEach { par ->
            generator.writeStartObject()
            generator.writeStringField("name", par.name)
            generator.writeStringField("type", par.type.javaType.typeName)
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

    private fun GameObject.writeRigidBody(generator: JsonGenerator) {
        if (rigidBody is RigidBody.EmptyRigidBody) {
            // If the rigid body is empty, we don't need to serialize it
            // We then serialize only the position
            generator.writeObjectField("position", this.position)
            return
        }
        generator.writeFieldName("rigidBody")
        generator.writeStartObject()
        generator.writeFieldName("shape")
        generator.writeStartObject()
        generator.writeObjectField("type", this.rigidBody.shape::class.qualifiedName!!)
        generator.writeObjectField("params", this.rigidBody.shape)
        generator.writeEndObject()
        generator.writeObjectField("mass", this.rigidBody.mass)
        generator.writeObjectField("velocity", this.rigidBody.velocity)
        generator.writeObjectField("static", this.rigidBody.isStatic)
        generator.writeEndObject()
    }

    private fun GameObject.writeComponents(generator: JsonGenerator) {
        generator.writeFieldName("components")
        generator.writeStartArray()
        this.components.forEach {
            generator.writeStartObject()

            val componentClass = it::class
            generator.writeStringField("class", componentClass.qualifiedName)

            check(isFirstParameterAGameObject(componentClass))

            writeObjectArguments(it, generator)
            generator.writeEndObject()
        }
        generator.writeEndArray()
    }

    private fun GameObject.writeRenderer(generator: JsonGenerator) {
        generator.writeFieldName("renderer")
        generator.writeStartObject()
        generator.writeStringField("class", this.renderer::class.qualifiedName!!)
        writeObjectArguments(this.renderer, generator)
        generator.writeEndObject()
    }


}