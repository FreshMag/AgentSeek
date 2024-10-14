package io.github.agentseek.common

/**
 * This class represents a Rectangle in two dimensions.
 */
class Rectangle2d : Shape2d {
    /**
     * Gets Height of the rectangle.
     *
     * @return Height of the rectangle.
     */
    val height: Int

    /**
     * Gets Width of the rectangle.
     *
     * @return Width of the rectangle.
     */
    val width: Int
    /**
     * Gets upper left vertex of the rectangle.
     *
     * @return Upper left vertex.
     */
    /**
     * Sets upper left vertex of the rectangle.
     *
     * @param upperLeft Vertex to be set.
     */
    var upperLeft: Point2d
    private var lowerLeft: Point2d
    private var upperRight: Point2d
    /**
     * Gets lower right vertex of the rectangle.
     *
     * @return Lower right vertex.
     */
    /**
     * Sets lower right vertex of the rectangle.
     *
     * @param lowerRight Vertex to be set.
     */
    var lowerRight: Point2d

    /**
     * This constructor is used to instantiate a 2D rectangle that is oriented
     * horizontally.
     *
     * @param width      Width of the rectangle.
     * @param height     Height of the rectangle.
     * @param upperLeftX X coordinate of the upper left vertex of the rectangle.
     * @param upperLeftY Y coordinate of the upper left vertex of the rectangle.
     */
    constructor(width: Int, height: Int, upperLeftX: Int, upperLeftY: Int) : super() {
        this.height = height
        this.width = width
        this.upperLeft = Point2d(upperLeftX.toDouble(), upperLeftY.toDouble())
        this.lowerLeft = Point2d(upperLeftX.toDouble(), (upperLeftY + height).toDouble())
        this.upperRight = Point2d((upperLeftX + width).toDouble(), upperLeftY.toDouble())
        this.lowerRight = upperLeft.sum(Vector2d(width.toDouble(), height.toDouble()))
    }

    /**
     * This constructor is used to instantiate a 2D rectangle whose orientation is
     * not "simply" horizontal.
     *
     * @param upperLeft  Upper left vertex of the rectangle.
     * @param lowerLeft  Lower left vertex of the rectangle.
     * @param lowerRight Lower right vertex of the rectangle.
     * @param upperRight Upper right vertex of the rectangle.
     */
    constructor(
        upperLeft: Point2d, lowerLeft: Point2d, lowerRight: Point2d,
        upperRight: Point2d
    ) : super() {
        this.upperLeft = upperLeft
        this.lowerLeft = lowerLeft
        this.lowerRight = lowerRight
        this.upperRight = upperRight
        this.width = Vector2d(upperLeft, upperRight).module().toInt()
        this.height = Vector2d(upperLeft, lowerLeft).module().toInt()
    }

    /**
     * {@inheritDoc}
     */
    override fun contains(position: Point2d): Boolean {
        return position.x > upperLeft.x && position.x < upperRight.x && position.y > upperLeft.y && position.y < lowerLeft.y
    }

    /**
     * Used to determine if a rectangle is contained completely in this rectangle.
     *
     * @param rectangle Rectangle inside this rectangle.
     * @return True if all vertices of the inner rectangle are contained into the
     * outer rectangle.
     */
    fun contains(rectangle: Rectangle2d): Boolean {
        return this.contains(rectangle.upperLeft) && this.contains(rectangle.lowerRight)
                && this.contains(rectangle.upperRight) && this.contains(rectangle.lowerLeft)
    }

    /**
     * {@inheritDoc}
     */
    override fun setPosition(position: Point2d) {
        val transform = Vector2d(this.upperLeft, position)
        this.upperLeft = upperLeft.sum(transform)
        this.upperRight = upperRight.sum(transform)
        this.lowerLeft = lowerLeft.sum(transform)
        this.lowerRight = lowerRight.sum(transform)
    }

    /**
     * {@inheritDoc}
     */
    override fun getPosition(): Point2d {
        return this.upperLeft
    }

    /**
     * {@inheritDoc}
     */
    override fun intersect(shape: Shape2d): Boolean {
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun getCenter(): Point2d {
        return Point2d(
            (upperLeft.x + lowerRight.x) / 2,
            upperLeft.y + lowerRight.y / 2
        )
    }
}