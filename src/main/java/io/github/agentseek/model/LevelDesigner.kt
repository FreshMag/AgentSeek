package io.github.agentseek.model

import io.github.agentseek.controller.Type
import io.github.agentseek.controller.core.GameEngine
import java.util.*
import java.util.function.Function
import kotlin.math.pow

/**
 * A class that determines which entities are going to be spawned.
 */
class LevelDesigner {
    private var levelNum = 0
    private val entities: MutableList<Type> = ArrayList<Type>()
    private val random: Random = Random()

    /**
     * This function represents how difficulty raises along with the amount of levels explored.
     */
    private val difficultyFunction: Function<Int, Int> =
        Function<Int, Int> { x: Int -> (200 * Math.E.pow(-(x).toDouble() / 7) - 80).toInt() }

    fun generateLevelEntities(): List<Type> {
        levelNum++
        if (levelNum == 1) {
            entities.clear()
        } else {
            this.generateObstacles()
            this.generateItems()
            this.generateEnemies()
        }
        return entities
    }

    /**
     * This method is used to determine a range that represents the difficulty of
     * the current level. It is a function of the [enemyNumber] of this level.
     */
    private fun getDifficultyRange(enemyNumber: Int): Int {
        return difficultyFunction.apply(enemyNumber)
    }

    /**
     * This method generates a list of Entity type, that represents the entities
     * inside current level.
     */
    private fun generateEnemies() {
        val enemyNumber = (random.nextGaussian() + 3).toInt()
        var difficultyRange = this.getDifficultyRange(enemyNumber)
        difficultyRange = difficultyRange / TROUBLE_DECREASER + levelNum * TROUBLE_INCREASER
        val difficulty = (random.nextGaussian() + difficultyRange).toInt()

        for (i in 0 until enemyNumber) {
            if (difficulty in 0..EASY_MODE) {
                if (random.nextInt(2) == 0) {
                    entities.add(Type.ENEMY_DRUNK)
                } else {
                    entities.add(Type.ENEMY_LURKER)
                }
            } else if (difficulty in (EASY_MODE + 1)..MEDIUM_MODE) {
                if (random.nextInt(2) == 0) {
                    entities.add(Type.ENEMY_SHOOTER)
                } else {
                    entities.add(Type.ENEMY_SPINNER)
                }
            } else if (difficulty > HARD_MODE) {
                if (random.nextInt(2) == 0) {
                    entities.add(Type.ENEMY_DRUNKSPINNER)
                } else {
                    entities.add(Type.ENEMY_DRUNKSPINNER)
                }
            }
        }
        GameEngine.runDebugger { println(entities) }
    }

    /**
     * This method add "ITEM" to the entity list of the current level. It does so
     * only every three levels.
     */
    private fun generateItems() {
        if (levelNum % 3 == 0) {
            entities.add(Type.ITEM)
        }
    }

    /**
     * Generates random obstacles choosing between "ROCK" type and "FIRE" type.
     */
    private fun generateObstacles() {
        val obstacleNum = random.nextGaussian().toInt() + 2
        for (i in 0 until obstacleNum) {
            if (random.nextInt(2) == 0) {
                entities.add(Type.FIRE)
            } else {
                entities.add(Type.ROCK)
            }
        }
    }

    /**
     * Clears the entity list of the current level.
     */
    fun clearLevel() {
        entities.clear()
    }

    /**
     * Sets current [level].
     * Note: this method should be used ONLY in developer mode.
     */
    fun setLevel(level: Int) {
        this.levelNum = level
    }

    companion object {
        /** Above this integer, difficulty range indicates "hard" as difficulty for this level. */
        private const val HARD_MODE = 50

        /** Above this integer, difficulty range indicates "medium" as difficulty for this level. */
        private const val MEDIUM_MODE = 50

        /** Above this integer, difficulty range indicates "easy" as difficulty for this level. */
        private const val EASY_MODE = 20

        /**
         * Value used to calculate type of enemies to spawn.
         */
        private const val TROUBLE_INCREASER = 2

        /**
         * Value used to calculate type of enemies to spawn.
         */
        private const val TROUBLE_DECREASER = 15
    }
}
