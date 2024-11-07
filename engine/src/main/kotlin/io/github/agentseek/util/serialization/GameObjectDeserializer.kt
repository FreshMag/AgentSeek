package io.github.agentseek.util.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import io.github.agentseek.common.*
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine.log
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.view.Renderer
import io.github.agentseek.world.World
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

internal class GameObjectDeserializer(private val world: World) : JsonDeserializer<GameObject>() {

    private fun GameObject.setRigidBodyFrom(rigidBodyNode: JsonNode) {
        val shapeClassName = rigidBodyNode["shape"]["type"].asText()
        val shape = jacksonObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }.treeToValue(rigidBodyNode["shape"]["params"], Class.forName(shapeClassName)) as Shape2d

        val mass = rigidBodyNode["mass"].asDouble()
        val velocityNode = rigidBodyNode["velocity"]
        val velocity = Vector2d(
            x = velocityNode["x"].asDouble(),
            y = velocityNode["y"].asDouble()
        )
        val isStatic = rigidBodyNode["static"].asBoolean()
        val rigidBody = when (shape) {
            is Circle2d -> RigidBody.CircleRigidBody(this, shape.radius)
            is Rectangle2d -> RigidBody.RectangleRigidBody(this, shape.width, shape.height)
            is Cone2d -> RigidBody.ConeRigidBody(this, shape.angle, shape.length, shape.rotation)
        }
        rigidBody.mass = mass
        rigidBody.isStatic = isStatic
        rigidBody.velocity = velocity
        this.rigidBody = rigidBody
        this.position = shape.position
    }

    private fun getArguments(parameters: List<KParameter>, argumentsNode: JsonNode): MutableMap<KParameter, Any?> {
        val args = mutableMapOf<KParameter, Any?>()

        parameters.forEach { param ->
            val paramNode = argumentsNode.find { it["name"].asText() == param.name }!!
            val value = try {
                deserializeParamNode<Any>(jacksonObjectMapper(), paramNode)
            } catch (e: ClassNotFoundException) {
                jacksonObjectMapper().treeToValue(paramNode.get("value"))
            }
            args[param] = value
        }
        return args
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(parser: JsonParser, context: DeserializationContext): GameObject {
        val node: JsonNode = parser.codec.readTree(parser)
        val go = GameObject(world = world)
        log(node.path("name")::class.toString())
        go.name = node["name"].asText()

        // Rigid body
        if (node.has("rigidBody")) {
            val rigidBodyNode = node["rigidBody"]
            go.setRigidBodyFrom(rigidBodyNode)
        } else {
            go.rigidBody = RigidBody.EmptyRigidBody(go)
        }

        // Components
        val componentsNode = node["components"]
        val components = componentsNode.map { compNode ->
            val componentClassName = compNode["class"].asText()
            val componentClass = Class.forName(componentClassName).kotlin as KClass<out Component>
            val constructor = componentClass.primaryConstructor!!
            val args = getArguments(constructor.parameters.drop(1), compNode["arguments"])

            args[constructor.parameters.first()] = go
            constructor.callBy(args)
        }

        // Renderer
        val rendererNode = node["renderer"]
        val rendererClassName = rendererNode["class"].asText()
        val rendererClass = Class.forName(rendererClassName).kotlin
        val rendererConstructor = rendererClass.primaryConstructor!!
        val rendererArgs = getArguments(rendererConstructor.parameters.drop(1), rendererNode)

        val renderer = rendererConstructor.callBy(rendererArgs) as Renderer<*>

        go.renderer = renderer
        components.forEach(go::addComponent)
        return go
    }

    companion object {
        private fun <T> deserializeParamNode(mapper: ObjectMapper, paramNode: JsonNode): T {
            val typeName = paramNode.get("type")!!.asText()
            val rawTypeName = typeName.substringBefore("<")
            val genericTypeName = typeName.substringAfter("<", "").substringBefore(">")
            val rawType = Class.forName(rawTypeName)

            val genericType = if (genericTypeName.isNotEmpty()) {
                Class.forName(genericTypeName)
            } else {
                null
            }
            val typeReference = object : TypeReference<T>() {
                override fun getType() = if (genericType != null) {
                    ParameterizedTypeImpl(rawType, arrayOf(genericType))
                } else {
                    rawType
                }
            }

            return mapper.readValue(paramNode["value"].toString(), typeReference)
        }

        private class ParameterizedTypeImpl(
            private val raw: Class<*>,
            private val args: Array<Type>
        ) : ParameterizedType {
            override fun getRawType() = raw
            override fun getActualTypeArguments() = args
            override fun getOwnerType(): Type? = null
        }
    }
}