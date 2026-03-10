package juko.game

import juko.BinomialQueue
import juko.dto.Human
import juko.dto.Mice

class BrockianUltraCricket(
    override val players: BinomialQueue<Mice>,
) : Cricket<Mice, Human>(), Brockian, UltraGame {
    override fun start() {
        TODO("Not yet implemented")
    }

    override fun finish() {
        TODO("Not yet implemented")
    }

    override fun result() {
        TODO("Not yet implemented")
    }

    override fun resultByPlayer(player: Mice) {
        TODO("Not yet implemented")
    }

    override fun bat(target: Hittable) {
        TODO("Not yet implemented")
    }

    override fun chooseTarget(targets: Collection<Human>): Human {
        TODO("Not yet implemented")
    }

    override fun runAway() {
        TODO("Not yet implemented")
    }

    override fun hitWithoutVisibleReason(target: Hittable) {
        TODO("Not yet implemented")
    }

    fun playRound(player: Mice, target: Human) {
        TODO("Not yet implemented")
    }
}
