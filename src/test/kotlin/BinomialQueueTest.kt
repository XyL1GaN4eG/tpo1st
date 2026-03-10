import juko.BinomialQueue
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BinomialQueueTest {
    companion object {
        @JvmStatic
        fun insertCases() = listOf(
            Arguments.of(listOf(1, 2, 3), 1, 3),
            Arguments.of(listOf(3, 2, 1, 0), 0, 4),
            Arguments.of(listOf(7), 7, 1),
        )

        @JvmStatic
        fun extractionCases() = listOf(
            Arguments.of(listOf(5, 1, 4, 2, 3)),
            Arguments.of(listOf(9, 9, 1, 5, 0)),
            Arguments.of(listOf(-1, -3, 2, 2, 0)),
        )

        @JvmStatic
        fun removeCases() = listOf(
            Arguments.of(listOf(4, 1, 4, 2, 3), 4, true, listOf(1, 2, 3, 4)),
            Arguments.of(listOf(5, 5, 5), 5, true, listOf(5, 5)),
            Arguments.of(listOf(1, 2, 3), 10, false, listOf(1, 2, 3)),
        )
    }

    private fun queueOf(elements: List<Int>): BinomialQueue<Int> =
        BinomialQueue<Int>().also { it.addAll(elements) }

    private fun drain(queue: BinomialQueue<Int>): List<Int> =
        buildList {
            while (true) {
                val value = queue.extractMin() ?: break
                add(value)
            }
        }

    @ParameterizedTest(name = "elements={0}, min={1}, size={2}")
    @MethodSource("insertCases")
    fun keepsMinimumAndSizeAfterInsertions(
        elements: List<Int>,
        expectedMin: Int,
        expectedSize: Int,
    ) {
        val queue = queueOf(elements)

        assertAll(
            { assertEquals(expectedMin, queue.min()) },
            { assertEquals(expectedSize, queue.size) },
        )
    }

    @ParameterizedTest(name = "extract order for {0}")
    @MethodSource("extractionCases")
    fun extractMinKeepsSortedOrderAndSize(elements: List<Int>) {
        val queue = queueOf(elements)
        val extracted = drain(queue)

        assertAll(
            { assertEquals(elements.sorted(), extracted) },
            { assertTrue(queue.isEmpty()) },
            { assertEquals(0, queue.size) },
            { assertEquals(null, queue.min()) },
        )
    }

    @ParameterizedTest(name = "remove {1} from {0}")
    @MethodSource("removeCases")
    fun removeDeletesSingleMatchingElement(
        elements: List<Int>,
        valueToRemove: Int,
        expectedRemoved: Boolean,
        expectedRemaining: List<Int>,
    ) {
        val queue = queueOf(elements)

        val removed = queue.remove(valueToRemove)
        val remainingSize = queue.size
        val remaining = drain(queue)

        assertAll(
            { assertEquals(expectedRemoved, removed) },
            { assertEquals(expectedRemaining.size, remainingSize) },
            { assertEquals(expectedRemaining, remaining) },
        )
    }

    @Test
    fun supportsMutableCollectionContract() {
        val queue = queueOf(listOf(4, 1, 4, 2, 3))

        assertTrue(queue.contains(4))
        assertTrue(queue.containsAll(listOf(1, 2, 3)))
        assertFalse(queue.contains(10))
        assertFalse(queue.addAll(emptyList()))

        assertTrue(queue.remove(4))
        assertTrue(queue.contains(4))
        assertEquals(4, queue.size)

        assertTrue(queue.removeAll(listOf(4, 10)))
        assertFalse(queue.contains(4))
        assertEquals(3, queue.size)

        assertTrue(queue.retainAll(listOf(2, 3)))
        assertEquals(2, queue.size)
        assertEquals(listOf(2, 3), listOf(queue.extractMin(), queue.extractMin()))

        queue.clear()
        assertTrue(queue.isEmpty())
        assertFalse(queue.remove(42))
    }
}
