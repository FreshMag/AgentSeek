package io.github.agentseek.components

import kotlin.reflect.KClass

/**
 * An annotation to specify the components that another component requires to function.
 * @param required The components that are required by the annotated component.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Requires(vararg val required: KClass<out Component>)
