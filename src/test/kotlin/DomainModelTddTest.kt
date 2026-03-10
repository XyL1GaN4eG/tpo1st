import juko.BinomialQueue
import juko.dto.Creature
import juko.dto.Human
import juko.dto.Mice
import juko.dto.MicePlayer
import juko.game.BrockianUltraCricket
import juko.game.Hittable
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DomainModelTddTest {
    private class RecordingTarget : Hittable {
        var hitCount = 0

        override fun receiveUnexpectedHit() {
            hitCount++
        }
    }

    companion object {
        @JvmStatic
        fun compareCases() = listOf(
            Arguments.of(10, 20, -1),
            Arguments.of(20, 10, 1),
            Arguments.of(10, 10, 0),
        )
    }

    @Test
    fun creatureCanManifestPhysicallyWithoutLosingIdentity() {
        val creature = object : Creature("Deep Thought") {}

        assertDoesNotThrow { creature.manifestPhysically() }
        assertEquals("Deep Thought", creature.name)
    }

    @Test
    fun humanCanReceiveUnexpectedHitAndRunAwayFromDanger() {
        val human = Human("Arthur Dent")

        assertDoesNotThrow {
            human.receiveUnexpectedHit()
            human.runAwayFromDanger()
        }
    }

    @Test
    fun miceCanHandleExistentialRoutine() {
        val mice = Mice("Frankie", 42)

        assertDoesNotThrow {
            mice.argueAboutMeaningOfLife()
            mice.resolveAllQuestionsOnceAndForAll()
        }
    }

    @Test
    fun miceCanPlayBrockianUltraCricketByHittingATarget() {
        val mice = Mice("Benjy", 7)
        val target = RecordingTarget()

        mice.playBrockianUltraCricket(target)

        assertEquals(1, target.hitCount)
    }

    @ParameterizedTest(name = "compare {0} to {1}")
    @MethodSource("compareCases")
    fun miceAreOrderedByScore(
        leftScore: Int,
        rightScore: Int,
        expectedSign: Int,
    ) {
        val left = Mice("Left", leftScore)
        val right = Mice("Right", rightScore)

        assertEquals(expectedSign, left.compareTo(right).compareTo(0))
    }

    @Test
    fun micePlayerCanEnterMatch() {
        val player = MicePlayer("Trillian", 100)

        assertDoesNotThrow { player.enterMatch() }
    }

    @Test
    fun brockianUltraCricketCanHandleBasicLifecycle() {
        val player = Mice("Slartibartfast", 1)
        val game = BrockianUltraCricket(BinomialQueue())

        assertDoesNotThrow {
            game.start()
            game.result()
            game.resultByPlayer(player)
            game.runAway()
            game.finish()
        }
    }

    @Test
    fun brockianUltraCricketBatHitsProvidedTarget() {
        val game = BrockianUltraCricket(BinomialQueue())
        val target = RecordingTarget()

        game.bat(target)

        assertEquals(1, target.hitCount)
    }

    @Test
    fun brockianUltraCricketCanHitWithoutVisibleReason() {
        val game = BrockianUltraCricket(BinomialQueue())
        val target = RecordingTarget()

        game.hitWithoutVisibleReason(target)

        assertEquals(1, target.hitCount)
    }

    @Test
    fun brockianUltraCricketChoosesOneOfAvailableHumans() {
        val game = BrockianUltraCricket(BinomialQueue())
        val first = Human("Ford Prefect")
        val second = Human("Zaphod Beeblebrox")

        val chosen = game.chooseTarget(listOf(first, second))

        assertTrue(chosen == first || chosen == second)
    }

    @Test
    fun brockianUltraCricketCanPlayARoundForPlayerAndTarget() {
        val game = BrockianUltraCricket(BinomialQueue())
        val player = Mice("Marvin", 0)
        val target = Human("Arthur Dent")

        assertDoesNotThrow { game.playRound(player, target) }
    }
}
