package ryleh.controller

/**
 * Interface for input control.
 */
interface InputController {
    /**
     * Initializes input keys and the actions to perform when a key is pressed or
     * released.
     */
    fun initInput()

    /**
     * Actions to perform every loop.
     */
    fun updateInput()
}
