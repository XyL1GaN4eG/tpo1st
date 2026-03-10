package juko.dto

import juko.game.Hittable

open class Mice(
    name: String,
    override val score: Number,
) : Creature(name), Playable, Comparable<Mice> {
    fun argueAboutMeaningOfLife() {
        TODO("Not yet implemented")
    }

    fun resolveAllQuestionsOnceAndForAll() {
        TODO("Not yet implemented")
    }

    fun playBrockianUltraCricket(target: Hittable) {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: Mice): Int {
        TODO("Not yet implemented")
    }
}
