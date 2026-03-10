package juko.dto

import juko.game.Hittable

open class Mice(
    name: String,
    override var score: Int,
) : Creature(name), Playable, Comparable<Mice> {
    fun argueAboutMeaningOfLife() {
        // no-op stub for domain model tests
    }

    fun resolveAllQuestionsOnceAndForAll() {
        // no-op stub for domain model tests
    }

    fun playBrockianUltraCricket(target: Hittable) {
        target.receiveUnexpectedHit()
    }

    fun awardPoint(points: Int = 1) {
        require(points >= 0) { "Points increment must be non-negative" }
        score += points
    }

    override fun compareTo(other: Mice): Int {
        return score.compareTo(other.score)
    }
}
