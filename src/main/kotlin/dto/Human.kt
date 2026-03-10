package juko.dto

import juko.game.Hittable

class Human(name: String) : Creature(name), Hittable {
    var unexpectedHitCount: Int = 0
        private set
    var isRunningAway: Boolean = false
        private set

    override fun receiveUnexpectedHit() {
        manifestPhysically()
        unexpectedHitCount++
        isRunningAway = false
    }

    fun runAwayFromDanger() {
        isRunningAway = true
    }
}
