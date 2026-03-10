package juko.game

import juko.BinomialQueue
import juko.dto.Human
import juko.dto.Mice
import juko.dto.MicePlayer

class BrockianUltraCricket : Cricket<Mice, Human>({ BinomialQueue() }), Brockian<Mice>, UltraGame {
    data class Round(
        val player: Mice,
        val target: Human,
        val scoreAfterHit: Int,
    )

    data class MatchResult(
        val leader: Mice?,
        val roundsPlayed: Int,
        val escapedTargets: Int,
        val finished: Boolean,
    )

    data class PlayerResult(
        val player: Mice,
        val score: Int,
        val successfulHits: Int,
        val isLeader: Boolean,
    )

    private var started = false
    private var finished = false
    private val rounds = mutableListOf<Round>()
    private val escapedTargets = linkedSetOf<Human>()
    private var lastTarget: Human? = null

    val isStarted: Boolean
        get() = started
    val isFinished: Boolean
        get() = finished
    val roundsPlayed: Int
        get() = rounds.size
    val lastResult: MatchResult?
        get() = computedResult
    val lastPlayerResult: PlayerResult?
        get() = computedPlayerResult

    private var computedResult: MatchResult? = null
    private var computedPlayerResult: PlayerResult? = null

    private fun requireActiveGame() {
        check(started && !finished) { "Game must be started before a round can be played" }
    }

    private fun performHit(
        player: Mice,
        target: Hittable,
    ) {
        requireActiveGame()
        check(hasPlayer(player)) { "Player must be added to the game before batting" }
        requeuePlayer(player) { striker -> striker.playBrockianUltraCricket(target) }
    }

    private fun recordRound(
        player: Mice,
        target: Human,
    ) {
        lastTarget = target
        rounds += Round(player, target, player.score)
        escapedTargets.remove(target)
    }

    override fun addPlayer(player: Mice): Boolean {
        val added = super.addPlayer(player)
        if (added && started && player is MicePlayer) {
            player.enterMatch()
        }
        return added
    }

    override fun removePlayer(player: Mice): Boolean {
        val removed = super.removePlayer(player)
        if (removed && player is MicePlayer) {
            player.leaveMatch()
        }
        return removed
    }

    override fun start() {
        check(!started) { "Game is already started" }
        check(players.isNotEmpty()) { "Game must have at least one player to start" }
        started = true
        finished = false
        snapshotPlayers().filterIsInstance<MicePlayer>().forEach { it.enterMatch() }
        computedResult = null
        computedPlayerResult = null
    }

    override fun finish() {
        check(!finished) { "Game is already finished" }
        check(started || rounds.isNotEmpty()) { "Game has not started yet" }
        started = false
        finished = true
        computedResult = MatchResult(
            leader = highestScoringPlayer(),
            roundsPlayed = rounds.size,
            escapedTargets = escapedTargets.size,
            finished = true,
        )
        snapshotPlayers().filterIsInstance<MicePlayer>().forEach { it.leaveMatch() }
    }

    override fun result() {
        computedResult = MatchResult(
            leader = highestScoringPlayer(),
            roundsPlayed = rounds.size,
            escapedTargets = escapedTargets.size,
            finished = finished,
        )
    }

    override fun resultByPlayer(player: Mice) {
        check(hasPlayer(player)) { "Player must be part of the game" }
        val leader = highestScoringPlayer()
        computedPlayerResult = PlayerResult(
            player = player,
            score = player.score,
            successfulHits = rounds.count { it.player === player },
            isLeader = player === leader,
        )
    }

    override fun bat(
        player: Mice,
        target: Human,
    ) {
        performHit(player, target)
        recordRound(player, target)
    }

    override fun chooseTarget(targets: Collection<Human>) =
        targets
            .filterNot { it.isRunningAway }
            .minWithOrNull(compareBy<Human>({ it.unexpectedHitCount }, { it.name }))
            ?: targets.minWithOrNull(compareBy<Human>({ it.unexpectedHitCount }, { it.name }))
            ?: throw NoSuchElementException("At least one target is required")

    override fun runAway() {
        val target = lastTarget ?: return
        target.runAwayFromDanger()
        escapedTargets += target
    }

    override fun hitWithoutVisibleReason(
        player: Mice,
        target: Hittable,
    ) {
        performHit(player, target)
        if (target is Human) {
            recordRound(player, target)
        }
    }

    fun playRound(
        player: Mice,
        target: Human,
    ) {
        bat(player, target)
        runAway()
    }
}
