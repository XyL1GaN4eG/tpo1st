package juko.game

import juko.BinomialQueue
import juko.dto.Human
import juko.dto.Mice

class BrockianUltraCricket : Cricket<Mice, Human>({ BinomialQueue() }), Brockian<Mice>, UltraGame {
    private var started = false

    private fun performHit(
        player: Mice,
        target: Hittable,
    ) {
        check(hasPlayer(player)) { "Player must be added to the game before batting" }
        removePlayer(player)
        player.awardPoint()
        addPlayer(player)
        target.receiveUnexpectedHit()
    }

    override fun start() {
        started = true
    }

    override fun finish() {
        started = false
    }

    override fun result() {
        check(started || players.isNotEmpty()) { "Game has not started yet" }
    }

    override fun resultByPlayer(player: Mice) {
        check(hasPlayer(player)) { "Player must be part of the game" }
    }

    override fun bat(
        player: Mice,
        target: Human,
    ) {
        performHit(player, target)
    }

    override fun chooseTarget(targets: Collection<Human>): Human {
        return targets.firstOrNull() ?: throw NoSuchElementException("At least one target is required")
    }

    override fun runAway() {
        // no-op stub for domain model tests
    }

    override fun hitWithoutVisibleReason(
        player: Mice,
        target: Hittable,
    ) {
        performHit(player, target)
    }

    fun playRound(
        player: Mice,
        target: Human,
    ) {
        bat(player, target)
        runAway()
    }
}
