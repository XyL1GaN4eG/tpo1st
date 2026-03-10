import juko.BinomialQueue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BinomialQueueTest {

    @Test
    fun create() {
        val queue = BinomialQueue<Int>()

        queue.add(1)
        queue.add(2)
        queue.add(3)
        assertEquals(1, queue.min())

        queue.add(0)
        assertEquals(0, queue.min())

        queue.remove(1)
    }
}