package io.github.agentseek.util.jason

import io.github.agentseek.common.Point2d
import jason.NoValueException
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.NumberTermImpl
import jason.asSyntax.Term

/**
 * Utility functions for converting between Jason literals and Vector2D/Point2D.
 */
object Utils {
    /**
     * Converts a Jason literal to a function with two numeric arguments.
     */
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

    /**
     * Converts a Jason literal to a Vector2D.
     */
    fun Point2d.toLiteral(functor: String): Literal =
        Literal.parseLiteral("$functor($x, $y)")

    /**
     * Converts a Jason [Term] to a number.
     */
    @Throws(NoValueException::class)
    fun Term.toNumber(): Number {
        require(this.isNumeric) { "Cannot parse as number: $this" }
        return (this as NumberTerm).solve()
    }

    /**
     * Converts a number to a Jason [Term].
     */
    fun Number.toTerm(): Term {
        return NumberTermImpl(this.toDouble())
    }
}
