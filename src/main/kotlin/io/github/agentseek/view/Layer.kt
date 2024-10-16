package io.github.agentseek.view

enum class Layer(val importance: Int) {
    UI(100),
    BACKGROUND(0),
    ENVIRONMENT(20),
    SFX(90),
    GENERIC(50)
}