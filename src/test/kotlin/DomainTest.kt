import juko.dto.Creature
import juko.dto.Human
import juko.dto.Mice
import juko.dto.MicePlayer
import juko.game.BrockianUltraCricket
import juko.game.Hittable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DomainTest {
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

        assertFalse(creature.isPhysicallyManifested)
        creature.manifestPhysically()
        assertTrue(creature.isPhysicallyManifested)
        assertEquals("Deep Thought", creature.name)
    }

    @Test
    fun humanCanReceiveUnexpectedHitAndRunAwayFromDanger() {
        val human = Human("Arthur Dent")

        human.receiveUnexpectedHit()
        assertTrue(human.isPhysicallyManifested)
        assertEquals(1, human.unexpectedHitCount)
        assertFalse(human.isRunningAway)

        human.runAwayFromDanger()
        assertTrue(human.isRunningAway)
    }

    @Test
    fun miceCanHandleExistentialRoutine() {
        val mice = Mice("Frankie", 42)

        mice.argueAboutMeaningOfLife()
        assertTrue(mice.isPhysicallyManifested)
        assertEquals(1, mice.meaningOfLifeArguments)
        assertFalse(mice.hasResolvedAllQuestions)

        mice.resolveAllQuestionsOnceAndForAll()
        assertEquals(0, mice.meaningOfLifeArguments)
        assertTrue(mice.hasResolvedAllQuestions)
    }

    @Test
    fun miceCanPlayBrockianUltraCricketByHittingATarget() {
        val mice = Mice("Benjy", 7)
        val target = RecordingTarget()

        mice.playBrockianUltraCricket(target)

        assertEquals(8, mice.score)
        assertEquals(1, mice.successfulHits)
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

        assertFalse(player.isInMatch)
        player.enterMatch()
        assertTrue(player.isPhysicallyManifested)
        assertEquals(1, player.enteredMatches)
        assertTrue(player.isInMatch)

        player.leaveMatch()
        assertFalse(player.isInMatch)
    }

    @Test
    fun brockianUltraCricketCanHandleBasicLifecycle() {
        val player = MicePlayer("Slartibartfast", 1)
        val game = BrockianUltraCricket()
        game.addPlayer(player)

        game.start()
        assertTrue(game.isStarted)
        assertTrue(player.isInMatch)

        game.result()
        assertNotNull(game.lastResult)
        assertEquals(player, game.lastResult!!.leader)

        game.resultByPlayer(player)
        assertNotNull(game.lastPlayerResult)
        assertEquals(player, game.lastPlayerResult!!.player)
        assertTrue(game.lastPlayerResult!!.isLeader)

        game.runAway()
        game.finish()
        assertFalse(game.isStarted)
        assertTrue(game.isFinished)
        assertFalse(player.isInMatch)
    }

    @Test
    fun brockianUltraCricketBatIncrementsPlayerScore() {
        val game = BrockianUltraCricket()
        val player = Mice("Wowbagger", 3)
        val target = Human("Arthur Dent")

        assertTrue(game.addPlayer(player))
        game.start()

        game.bat(player, target)

        assertEquals(4, player.score)
        assertEquals(1, target.unexpectedHitCount)
        assertEquals(1, game.roundsPlayed)
    }

    @Test
    fun brockianUltraCricketCanHitWithoutVisibleReason() {
        val game = BrockianUltraCricket()
        val player = Mice("Agrajag", 9)
        val target = RecordingTarget()

        game.addPlayer(player)
        game.start()
        game.hitWithoutVisibleReason(player, target)

        assertEquals(10, player.score)
        assertEquals(1, target.hitCount)
        assertEquals(0, game.roundsPlayed)
    }

    @Test
    fun brockianUltraCricketChoosesBestAvailableHumanTarget() {
        val game = BrockianUltraCricket()
        val first = Human("Ford Prefect")
        val second = Human("Zaphod Beeblebrox")
        val third = Human("Arthur Dent")

        first.receiveUnexpectedHit()
        first.runAwayFromDanger()
        second.receiveUnexpectedHit()

        val chosen = game.chooseTarget(listOf(first, second, third))

        assertSame(third, chosen)
    }

    @Test
    fun gameStoresPlayersInternallyThroughAddPlayerAndRemovePlayer() {
        val game = BrockianUltraCricket()
        val player = Mice("Marvin", 0)

        assertTrue(game.addPlayer(player))
        assertFalse(game.addPlayer(player))
        assertTrue(player in game.players)
        assertTrue(game.removePlayer(player))
        assertTrue(player !in game.players)
    }

    @Test
    fun brockianUltraCricketCanPlayARoundForPlayerAndTarget() {
        val game = BrockianUltraCricket()
        val player = Mice("Marvin", 0)
        val target = Human("Arthur Dent")

        game.addPlayer(player)
        game.start()

        game.playRound(player, target)
        assertEquals(1, player.score)
        assertEquals(1, target.unexpectedHitCount)
        assertTrue(target.isRunningAway)
        assertEquals(1, game.roundsPlayed)

        game.result()
        assertEquals(1, game.lastResult!!.roundsPlayed)
        assertEquals(1, game.lastResult!!.escapedTargets)

        game.resultByPlayer(player)
        assertEquals(1, game.lastPlayerResult!!.successfulHits)
    }
}
