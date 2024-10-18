package io.github.agentseek.common

/**
 * This class represents a Rectangle in two dimensions.
 */
data class Rectangle2d(
    var upperLeft: Point2d,
    var lowerLeft: Point2d,
    var lowerRight: Point2d,
    var upperRight: Point2d,
) : Shape2d {
    /**
     * The height of the rectangle.
     */
    private val height: Double = Vector2d(upperLeft, lowerLeft).module()

    /**
     * Gets Width of the rectangle.
     *
     * @return Width of the rectangle.
     */
    private val width: Double = Vector2d(upperLeft, upperRight).module()

    /**
     * This constructor is used to instantiate a 2D rectangle that is oriented
     * horizontally.
     *
     * @param width      Width of the rectangle.
     * @param height     Height of the rectangle.
     * @param upperLeftX X coordinate of the upper left vertex of the rectangle.
     * @param upperLeftY Y coordinate of the upper left vertex of the rectangle.
     */
    constructor(width: Double, height: Double, upperLeftX: Double, upperLeftY: Double) : this(
        Point2d(upperLeftX, upperLeftY),
        Point2d(upperLeftX, (upperLeftY + height)),
        Point2d((upperLeftX + width), upperLeftY),
        Point2d(upperLeftX + width, upperLeftY + height)
    )

    constructor(width: Double, height: Double) : this(width, height, 0.0, 0.0)

    override fun contains(point: Point2d): Boolean =
        point.x > upperLeft.x && point.x < upperRight.x && point.y > upperLeft.y && point.y < lowerLeft.y


    /**
     * Used to determine if a rectangle is contained completely in this rectangle.
     *
     * @param rectangle Rectangle inside this rectangle.
     * @return True if all vertices of the inner rectangle are contained into the
     * outer rectangle.
     */
    fun contains(rectangle: Rectangle2d): Boolean =
        this.contains(rectangle.upperLeft) && this.contains(rectangle.lowerRight) && this.contains(rectangle.upperRight) && this.contains(
            rectangle.lowerLeft
        )


    override var position: Point2d
        get() = upperLeft
        set(value) {
            val transform = Vector2d(this.upperLeft, value)
            this.upperLeft += (transform)
            this.upperRight += (transform)
            this.lowerLeft += (transform)
            this.lowerRight += (transform)
        }

    override fun intersects(shape: Shape2d): Boolean = TODO()

    override var center: Point2d
        get() = Point2d(
            (upperLeft.x + lowerRight.x) / 2, upperLeft.y + lowerRight.y / 2
        )
        set(value) {}
}