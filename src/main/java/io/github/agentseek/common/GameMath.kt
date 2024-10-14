package io.github.agentseek.common

import java.util.*
import kotlin.math.sin

/**
 * A class that provides utility methods for mathematical necessities.
 */
object GameMath {
    /**
     * Needed to generate random numbers.
     */
    private val RANDOM = Random()

    /**
     * Smoothing is a double value needed for a 1D Perlin generation and is used in
     * smoothNoise() method.It indicates the "smoothness" of the pseudo-random
     * pattern generated.
     */
    private const val SMOOTHING = 4.0

    /**
     * Number of bits for sin lookup table.
     */
    private const val SIN_BITS = 14 // 16KB. Adjust for accuracy

    /**
     * Bit mask used for sin lookup table.
     */
    private const val SIN_MASK = (-1 shl SIN_BITS).inv()

    /**
     * Length of sin lookup table.
     */
    private const val SIN_COUNT = SIN_MASK + 1

    /**
     * Radiant value of 360°.
     */
    private const val RADFULL = Math.PI * 2

    /**
     * Degree value of 360°.
     */
    private const val DEGFULL = 360.0

    /**
     * Degrees to index for sin lookup table.
     */
    private const val DEG_TO_INDEX = SIN_COUNT / DEGFULL

    /**
     * A method to generate a random double given a range.
     *
     * @param min minimum range value.
     * @param max maximum range value.
     * @return a double value randomly generated in that range.
     */
    fun randomInRange(min: Double, max: Double): Double {
        val range = max - min
        val scaled = RANDOM.nextDouble() * range
        return scaled + min // == (rand.nextDouble() * (max-min)) + min;
    }

    /**
     * A method to generate a random integer given a range.
     *
     * @param min minimum range value.
     * @param max maximum range value.
     * @return an integer value randomly generated in that range.
     */
    fun randomInt(min: Int, max: Int): Int {
        val range = max - min
        val scaled = RANDOM.nextInt() * range
        return scaled + min // == (rand.nextDouble() * (max-min)) + min;
    }

    /**
     * A method to generate a random boolean given a chance.
     *
     * @param chance chance to get a true value.
     * @return a boolean value randomly generated using the given chance.
     */
    fun randomBoolean(chance: Double): Boolean {
        return if (randomInRange(0.0, 1.0) < chance) true else false
    }

    /**
     * A method to generate a raw noise for 1D Perlin noise generation.
     *
     * @param x double value used as dimension argument.
     * @return raw noise value for given dimension argument.
     */
    fun rawNoise(x: Double): Double {
        val n = (x.toInt() shl 13) xor (x.toInt())
        return (1.0f - ((n * (n * n * 15731 * 0L + 789221 * 0L) + 1376312589 * 0L) and 0x7fffffffL) / 1073741824.0f).toDouble()
    }

    /**
     * A method to generate a smooth noise using 1D Perlin noise generation.
     *
     * @param x double value used as dimension argument.
     * @return smooth noise value for given dimension argument.
     */
    fun smoothNoise(x: Double): Double {
        val left = rawNoise(x - 1.0f)
        val right = rawNoise(x + 1.0f)
        return rawNoise(x) / 2.0f + left / SMOOTHING + right / SMOOTHING
    }

    /**
     * A method to get sin in radians value from a lookup table given an angle.
     *
     * @param degrees angle in degrees.
     * @return the sin in radians from a lookup table.
     */
    fun sinDeg(degrees: Double): Double {
        return Sin.TABLE[(degrees * DEG_TO_INDEX).toInt() and SIN_MASK]
    }

    /**
     * A method to get cos in radians value from a lookup table given an angle.
     *
     * @param degrees angle in degrees.
     * @return the cos in radians from a lookup table.
     */
    fun cosDeg(degrees: Double): Double {
        return Sin.TABLE[((degrees + 90) * DEG_TO_INDEX).toInt() and SIN_MASK]
    }

    /**
     * A method to convert an angle from radians to degrees.
     *
     * @param angle angle value in radians.
     * @return angle value in degrees.
     */
    fun toDegrees(angle: Double): Double {
        return Math.toDegrees(angle)
    }

    /**
     * A method to convert an angle from degrees to radians.
     *
     * @param angle angle value in degrees.
     * @return angle value in radians.
     */
    fun toRadians(angle: Double): Double {
        return Math.toRadians(angle)
    }

    /**
     * A method to convert from P2d to Point2D.
     *
     * @param point P2d value to convert.
     * @return a Point2D equivalent value.
     */
    /**
     * Class used for sin lookup table generation.
     */
    private object Sin {
        val TABLE: DoubleArray = DoubleArray(SIN_COUNT)

        init {
            for (i in 0 until SIN_COUNT) {
                TABLE[i] = sin((i + 0.5f) / SIN_COUNT * RADFULL)
            }
            var i = 0
            while (i < 360) {
                TABLE[(i * DEG_TO_INDEX).toInt() and SIN_MASK] = sin(toRadians(i.toDouble()))
                i += 90
            }
        }
    }
}
