package io.github.agentseek.util.jason

import jason.NoValueException
import jason.asSyntax.NumberTerm
import jason.asSyntax.NumberTermImpl
import jason.asSyntax.Term

object Utils {

    @Throws(NoValueException::class)
    fun termToNumber(term: Term): Double {
        require(term.isNumeric) { "Cannot parse as number: $term" }
        return (term as NumberTerm).solve()
    }

    @Throws(NoValueException::class)
    fun termToInteger(term: Term): Int {
        return termToNumber(term).toInt()
    }

    fun numberToTerm(value: Int): Term {
        return NumberTermImpl(value.toDouble())
    }

    fun numberToTerm(value: Double): Term {
        return NumberTermImpl(value)
    }
}
