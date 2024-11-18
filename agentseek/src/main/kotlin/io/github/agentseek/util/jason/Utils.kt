package io.github.agentseek.util.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.vector
import jason.NoValueException
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.NumberTermImpl
import jason.asSyntax.Term

object Utils {

    private fun <T> Literal.toBiFunction(function: (Number, Number) -> T): T {
        require(!(!getTerm(0).isNumeric || !getTerm(1).isNumeric)) { "Cannot parse as numeric bi function: $this" }
        try {
            return function(
                getTerm(0).toNumber(),
                getTerm(1).toNumber()
            )
        } catch (e: NoValueException) {
            throw IllegalArgumentException("Cannot parse as Vector2D: $this")
        }
    }

    fun Literal.toVector(): Vector2d =
        toBiFunction { x, y -> vector(x, y) }

    fun Literal.toPoint(): Point2d =
        toBiFunction { x, y -> point(x, y) }

    fun Vector2d.toLiteral(functor: String): Literal =
        Literal.parseLiteral("$functor($x, $y)")

    fun Point2d.toLiteral(functor: String): Literal =
        Literal.parseLiteral("$functor($x, $y)")

    @Throws(NoValueException::class)
    fun Term.toNumber(): Number {
        require(this.isNumeric) { "Cannot parse as number: $this" }
        return (this as NumberTerm).solve()
    }

    fun Number.toTerm(): Term {
        return NumberTermImpl(this.toDouble())
    }
}
