package ryleh.common;

import java.util.Random;

import javafx.geometry.Point2D;
import ryleh.view.ViewHandlerImpl;

/**
 * A class that provides utility methods for mathematical necessities.
 */
public final class GameMath {
    /**
     * Needed to generate random numbers.
     */
    private static final Random RANDOM = new Random();
    /**
     * Smoothing is a double value needed for a 1D Perlin generation and is used in
     * smoothNoise() method.It indicates the "smoothness" of the pseudo-random
     * pattern generated.
     */
    private static final double SMOOTHING = 4.0f;
    /**
     * Number of bits for sin lookup table.
     */
    private static final int SIN_BITS = 14; // 16KB. Adjust for accuracy
    /**
     * Bit mask used for sin lookup table.
     */
    private static final int SIN_MASK = ~(-1 << SIN_BITS);
    /**
     * Length of sin lookup table.
     */
    private static final int SIN_COUNT = SIN_MASK + 1;
    /**
     * Radiant value of 360°.
     */
    private static final double RADFULL = Math.PI * 2;
    /**
     * Degree value of 360°.
     */
    private static final double DEGFULL = 360;
    /**
     * Degrees to index for sin lookup table.
     */
    private static final double DEG_TO_INDEX = SIN_COUNT / DEGFULL;

    /**
     * Degrees to radians conversion.
     */
    // private static final double DEGREES_TO_RADIANS = Math.PI / 180;

    private GameMath() {
    }

    /**
     * Class used for sin lookup table generation.
     */
    private static class Sin {
        protected static final double[] TABLE = new double[SIN_COUNT];
        static {
            for (int i = 0; i < SIN_COUNT; i++) {
                TABLE[i] = Math.sin((i + 0.5f) / SIN_COUNT * RADFULL);
            }
            for (int i = 0; i < 360; i += 90) {
                TABLE[(int) (i * DEG_TO_INDEX) & SIN_MASK] = Math.sin(toRadians(i));
            }
        }
    }

    /**
     * A method to generate a random double given a range.
     * 
     * @param min minimum range value.
     * @param max maximum range value.
     * @return a double value randomly generated in that range.
     */
    public static double randomInRange(final double min, final double max) {
        final double range = max - min;
        final double scaled = RANDOM.nextDouble() * range;
        return scaled + min; // == (rand.nextDouble() * (max-min)) + min;
    }

    /**
     * A method to generate a random integer given a range.
     * 
     * @param min minimum range value.
     * @param max maximum range value.
     * @return an integer value randomly generated in that range.
     */
    public static int randomInt(final int min, final int max) {
        final int range = max - min;
        final int scaled = RANDOM.nextInt() * range;
        return scaled + min; // == (rand.nextDouble() * (max-min)) + min;
    }

    /**
     * A method to generate a random boolean given a chance.
     * 
     * @param chance chance to get a true value.
     * @return a boolean value randomly generated using the given chance.
     */
    public static boolean randomBoolean(final double chance) {
        return randomInRange(0, 1) < chance ? true : false;
    }

    /**
     * A method to generate a raw noise for 1D Perlin noise generation.
     * 
     * @param x double value used as dimension argument.
     * @return raw noise value for given dimension argument.
     */
    public static double rawNoise(final double x) {
        final int n = ((int) x << 13) ^ ((int) x);
        return 1.0f
                - ((n * (n * n * 15_731 * 0L + 789_221 * 0L) + 1_376_312_589 * 0L) & 0x7fffffff) / 1_073_741_824.0f;
    }

    /**
     * A method to generate a smooth noise using 1D Perlin noise generation.
     * 
     * @param x double value used as dimension argument.
     * @return smooth noise value for given dimension argument.
     */
    public static double smoothNoise(final double x) {
        final double left = rawNoise(x - 1.0f);
        final double right = rawNoise(x + 1.0f);
        return rawNoise(x) / 2.0f + left / SMOOTHING + right / SMOOTHING;
    }

    /**
     * A method to get sin in radians value from a lookup table given an angle.
     * 
     * @param degrees angle in degrees.
     * @return the sin in radians from a lookup table.
     */
    public static double sinDeg(final double degrees) {
        return Sin.TABLE[(int) (degrees * DEG_TO_INDEX) & SIN_MASK];
    }

    /**
     * A method to get cos in radians value from a lookup table given an angle.
     * 
     * @param degrees angle in degrees.
     * @return the cos in radians from a lookup table.
     */
    public static double cosDeg(final double degrees) {
        return Sin.TABLE[(int) ((degrees + 90) * DEG_TO_INDEX) & SIN_MASK];
    }

    /**
     * A method to convert an angle from radians to degrees.
     * 
     * @param angle angle value in radians.
     * @return angle value in degrees.
     */
    public static double toDegrees(final double angle) {
        return Math.toDegrees(angle);
    }

    /**
     * A method to convert an angle from degrees to radians.
     * 
     * @param angle angle value in degrees.
     * @return angle value in radians.
     */
    public static double toRadians(final double angle) {
        return Math.toRadians(angle);
    }

    /**
     * A method to convert from P2d to Point2D.
     * 
     * @param point P2d value to convert.
     * @return a Point2D equivalent value.
     */
    public static Point2D toPoint2D(final Point2d point) {
       return new Point2D(point.getX() * ViewHandlerImpl.getScaleModifier(), point.getY() * ViewHandlerImpl.getScaleModifier());
    }
}
