package io.github.agentseek.common

/**
 * This class represents a Rectangle in two dimensions.
 */
data class Rectangle2d(
    var upperLeft: Point2d,
    var lowerLeft: Point2d,
    var lowerRight: Point2d,
    var upperRight: Point2d,
) : Shape2d() {
    /**
     * The height of the rectangle.
     */
    override val height: Double = (lowerRight.y - upperRight.y)

    /**
     * Gets Width of the rectangle.
     *
     * @return Width of the rectangle.
     */
    override val width: Double = (lowerRight.x - lowerLeft.x)

    constructor(width: Double, height: Double) : this(
        Point2d(0.0, 0.0),
        width,
        height,
    )

    constructor(upperLeft: Point2d, width: Double, height: Double) : this(
        upperLeft,
        upperLeft + Vector2d(0.0, height),
        upperLeft + Vector2d(width, height),
        upperLeft + Vector2d(width, 0.0)
    )

    override fun contains(point: Point2d): Boolean =
        point.x > upperLeft.x && point.x < upperRight.x && point.y > upperLeft.y && point.y < lowerLeft.y

    /**
     * Returns an iterable of [Point2d] representing the vertices, clockwise ordered, starting from [upperLeft]
     */
    fun vertices(): Iterable<Point2d> =
        listOf(upperLeft, upperRight, lowerRight, lowerLeft)

    /**
     * Returns an iterable of edges, that is pairs of [Point2d] representing start and finish of each edge
     */
    fun edges(): Iterable<Pair<Point2d, Point2d>> = listOf(
        Pair(upperLeft, upperRight),
        Pair(lowerLeft, lowerRight),
        Pair(upperLeft, lowerLeft),
        Pair(upperRight, lowerRight),
    )

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
            val transform = value - upperLeft
            this.upperLeft += (transform)
            this.upperRight += (transform)
            this.lowerLeft += (transform)
            this.lowerRight += (transform)
        }

    override fun intersects(shape: Shape2d): Boolean =
        when (shape) {
            is Rectangle2d -> intersectWithRectangle(shape)
            is Circle2d -> intersectWithCircle(shape)
            is Cone2d -> false
        }

    private fun intersectWithRectangle(rect: Rectangle2d): Boolean {
        return !(upperRight.x < rect.upperLeft.x || rect.upperRight.x < upperLeft.x ||
                lowerLeft.y < rect.upperLeft.y || rect.lowerLeft.y < upperLeft.y)
    }

    private fun intersectWithCircle(circle: Circle2d): Boolean {
        val closestX = circle.center.x.coerceIn(upperLeft.x, upperLeft.x + width)
        val closestY = circle.center.y.coerceIn(upperLeft.y, upperLeft.y + height)

        val distanceX = circle.center.x - closestX
        val distanceY = circle.center.y - closestY

        return distanceX * distanceX + distanceY * distanceY < circle.radius * circle.radius
    }

    override var center: Point2d = Point2d((upperLeft.x + upperRight.x) / 2, (upperLeft.y + lowerLeft.y) / 2)
        get() = Point2d((upperLeft.x + upperRight.x) / 2, (upperLeft.y + lowerLeft.y) / 2)
        set(value) {
            position = Point2d(value.x - (width / 2), value.y - (height / 2))
            field = value
        }
}