package juko.dto

import juko.game.Hittable

class Human(name: String) : Creature(name), Hittable {
    override fun receiveUnexpectedHit() {
        // no-op stub for domain model tests
    }

    fun runAwayFromDanger() {
        // no-op stub for domain model tests
    }
}
