
package io.github.agentseek.common

import kotlin.math.*

/**
 * This class represents a cone in two dimensions.
 */
data class Cone2d(
    var vertex: Point2d,
    /** Cone aperture (cone angle in radians) */
    var angle: Double,
    /** Cone extension */
    var length: Double,
    /** Rotation angle (in radians) */
    var rotation: Double
) : Shape2d() {

    private val triangle
        get() = toTriangle()

    override val width: Double
        get() = length * cos(rotation)
    override val height: Double
        get() = length * sin(rotation)

    private val direction: Vector2d
        get() = Vector2d(cos(angle), sin(angle))

    override fun contains(point: Point2d): Boolean {
        val (v1, v2, v3) = triangle
        val d1 = sign(point, v1, v2)
        val d2 = sign(point, v2, v3)
        val d3 = sign(point, v3, v1)
        val hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0)
        val hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0)
        return !(hasNeg && hasPos)
    }

    override var center: Point2d = vertex
        get() = vertex + (direction * (height / 2))
        set(value) {
            field = value
            vertex = value - (direction * height / 2.0)
        }

    override var position: Point2d = vertex
        get() = vertex
        set(value) {
            vertex = value
            field = value
        }

    /**
     * Returns the vertices of the triangle version of the cone.
     */
    fun edges(): Iterable<Pair<Point2d, Point2d>> {
        val (v1, v2, v3) = triangle
        return listOf(
            Pair(v1, v2),
            Pair(v2, v3),
            Pair(v3, v1)
        )
    }

    override fun intersects(shape: Shape2d): Boolean {
        return when (shape) {
            is Circle2d -> intersectWithCircle(shape)
            is Rectangle2d -> intersectWithRectangle(shape)
            is Cone2d -> intersectWithCone(shape)
        }
    }

    private fun toTriangle(): List<Point2d> {
        val halfAngle = angle / 2
        val vertex = this.vertex

        val leftBaseX = cos(rotation - halfAngle) * length + vertex.x
        val leftBaseY = sin(rotation - halfAngle) * length + vertex.y
        val rightBaseX = cos(rotation + halfAngle) * length + vertex.x
        val rightBaseY = sin(rotation + halfAngle) * length + vertex.y

        return listOf(vertex, Point2d(leftBaseX, leftBaseY), Point2d(rightBaseX, rightBaseY))
    }

    private fun intersectWithCircle(circle: Circle2d): Boolean {
        if (contains(circle.center)) return true

        return triangle.indices.any {
            val p1 = triangle[it]
            val p2 = triangle[(it + 1) % triangle.size]

            circleIntersectsLineSegment(circle, p1, p2)
        }
    }

    private fun intersectWithRectangle(rect: Rectangle2d): Boolean {
        if (rect.vertices().any { contains(it) }) return true

        val rectangleEdges = rect.edges()

        for ((p1, p2) in rectangleEdges) {
            for ((q1, q2) in edges()) {
                if (lineSegmentsIntersect(p1, p2, q1, q2)) return true
            }
        }

        return false
    }

    private fun intersectWithCone(other: Cone2d): Boolean {
        if (other.triangle.any { contains(it) }) return true
        if (triangle.any { other.contains(it) }) return true

        edges().forEach { (p1, p2) ->
            val result = other.edges().any { (q1, q2) -> lineSegmentsIntersect(p1, p2, q1, q2) }
            if (result) return true
        }

        return false
    }

    companion object {
        private fun lineSegmentsIntersect(p1: Point2d, p2: Point2d, q1: Point2d, q2: Point2d): Boolean {
            val o1 = orientation(p1, p2, q1)
            val o2 = orientation(p1, p2, q2)
            val o3 = orientation(q1, q2, p1)
            val o4 = orientation(q1, q2, p2)

            return (o1 != o2 && o3 != o4)
        }

        private fun orientation(p: Point2d, q: Point2d, r: Point2d): Int {
            val value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
            return when {
                value == 0.0 -> 0
                value > 0 -> 1
                else -> 2
            }
        }

        private fun sign(p1: Point2d, p2: Point2d, p3: Point2d): Double {
            return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)
        }

        private fun circleIntersectsLineSegment(circle: Circle2d, p1: Point2d, p2: Point2d): Boolean {
            val closestPoint = getClosestPointOnSegment(p1, p2, circle.center)
            return (closestPoint - circle.center).module() <= circle.radius
        }

        private fun getClosestPointOnSegment(p1: Point2d, p2: Point2d, point: Point2d): Point2d {
            val lineVec = p2 - p1
            val t = ((point - p1).dot(lineVec)) / (lineVec.dot(lineVec))
            val clampedT = t.coerceIn(0.0, 1.0)
            return p1 + lineVec * clampedT
        }
    }
}
