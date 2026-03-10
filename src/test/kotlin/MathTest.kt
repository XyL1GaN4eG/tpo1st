import juko.mymath.Arctg
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sqrt

class MathTest {
    companion object {
        private const val DELTA = 1e-12

        @JvmStatic
        fun arctgCases() = listOf(
            Arguments.of(0.0, 0.0),
            Arguments.of(1.0, PI / 4),
            Arguments.of(-1.0, -PI / 4),
            Arguments.of(sqrt(3.0), PI / 3),
            Arguments.of(-sqrt(3.0), -PI / 3),
            Arguments.of(0.5, atan(0.5)),
            Arguments.of(-0.5, atan(-0.5)),
            Arguments.of(10.0, atan(10.0)),
        )

        @JvmStatic
        fun finiteCases() = listOf(0.0, 0.25, -0.25, 1.0, -1.0, 100.0, -100.0)

        @JvmStatic
        fun specialCases() = listOf(
            Arguments.of(Double.POSITIVE_INFINITY, PI / 2),
            Arguments.of(Double.NEGATIVE_INFINITY, -PI / 2),
            Arguments.of(Double.NaN, Double.NaN),
        )

        @JvmStatic
        fun oddnessCases() = listOf(0.1, 0.5, 1.0, sqrt(3.0), 10.0)

        @JvmStatic
        fun monotonicCases() = listOf(
            Arguments.of(-10.0, -1.0),
            Arguments.of(-1.0, -0.1),
            Arguments.of(-0.1, 0.0),
            Arguments.of(0.0, 0.1),
            Arguments.of(0.1, 1.0),
            Arguments.of(1.0, 10.0),
        )
    }

    private val math = Arctg()

    @ParameterizedTest(name = "arctg({0})")
    @MethodSource("arctgCases")
    fun computesArctgUsingPowerSeries(
        input: Double,
        expected: Double,
    ) {
        assertEquals(expected, math.myArctg(input), DELTA)
    }

    @ParameterizedTest(name = "arctg({0}) is finite")
    @MethodSource("finiteCases")
    fun returnsFiniteValueForFiniteInput(input: Double) {
        assertTrue(math.myArctg(input).isFinite())
    }

    @ParameterizedTest(name = "special case {0}")
    @MethodSource("specialCases")
    fun handlesSpecialValues(
        input: Double,
        expected: Double,
    ) {
        if (expected.isNaN()) {
            assertTrue(math.myArctg(input).isNaN())
        } else {
            assertEquals(expected, math.myArctg(input), 0.0)
        }
    }

    @ParameterizedTest(name = "arctg(-{0}) = -arctg({0})")
    @MethodSource("oddnessCases")
    fun isOddFunction(input: Double) {
        assertEquals(-math.myArctg(input), math.myArctg(-input), DELTA)
    }

    @ParameterizedTest(name = "arctg({0}) < arctg({1})")
    @MethodSource("monotonicCases")
    fun isMonotonic(
        left: Double,
        right: Double,
    ) {
        assertTrue(math.myArctg(left) < math.myArctg(right))
    }
}
