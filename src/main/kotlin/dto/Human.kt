package juko.dto

import juko.game.Hittable

class Human(name: String) : Creature(name), Hittable {
    override fun receiveUnexpectedHit() {
        TODO("Not yet implemented")
    }

    fun runAwayFromDanger() {
        TODO("Not yet implemented")
    }
}
