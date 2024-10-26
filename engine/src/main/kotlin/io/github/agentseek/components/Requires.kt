package io.github.agentseek.components

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Requires(vararg val required: KClass<out Component>)
