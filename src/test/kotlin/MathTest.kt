import juko.MyMath
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class MathTest() {
    val lol = MyMath()

    @Test
    fun zero() {
        assertEquals(lol.myArctg(0.0), 0.0)
    }

    @Test
    fun one() {
        assertEquals(PI.div(4), lol.myArctg(1.0))
    }

    @Test
    fun `sqrt of three`() {
        assertEquals(PI.div(3), lol.myArctg(sqrt(3.0)))
    }
}