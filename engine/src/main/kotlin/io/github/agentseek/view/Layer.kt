package io.github.agentseek.view

/**
 * Represents the layer of a GameObject. The higher the importance, the more in front it will be rendered.
 */
enum class Layer(val importance: Int) {
    UI(100),
    BACKGROUND(0),
    ENVIRONMENT(20),
    SFX(90),
    GENERIC(50)
}