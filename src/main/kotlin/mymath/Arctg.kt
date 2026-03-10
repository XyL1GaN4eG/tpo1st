package juko.mymath

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt


class Arctg() {
    private companion object {
        const val EPSILON = 1e-15
        const val SERIES_THRESHOLD = 0.5
        const val MAX_ITERATIONS = 1_000_000
    }

    fun myArctg(number: Double): Double {
        if (number.isNaN()) return Double.NaN
        if (number == Double.POSITIVE_INFINITY) return PI / 2
        if (number == Double.NEGATIVE_INFINITY) return -PI / 2
        if (number == 0.0) return 0.0

        return if (number < 0) {
            -arctgPositive(-number)
        } else {
            arctgPositive(number)
        }
    }

    private fun arctgPositive(number: Double): Double {
        if (number > 1.0) {
            return PI / 2 - arctgPositive(1 / number)
        }

        if (number > SERIES_THRESHOLD) {
            val reduced = number / (1 + sqrt(1 + number * number))
            return 2 * arctgPositive(reduced)
        }

        return arctgSeries(number)
    }

    private fun arctgSeries(number: Double): Double {
        var term = number
        var sum = term
        var n = 0

        while (abs(term) > EPSILON && n < MAX_ITERATIONS) {
            term *= -(number * number) * (2 * n + 1).toDouble() / (2 * n + 3).toDouble()
            sum += term
            n++
        }

        return sum
    }

}
