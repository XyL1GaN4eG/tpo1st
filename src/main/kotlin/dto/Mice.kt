package juko.dto

import juko.game.Hittable

open class Mice(
    name: String,
    override var score: Int,
) : Creature(name), Playable, Comparable<Mice> {
    var meaningOfLifeArguments: Int = 0
        private set
    var hasResolvedAllQuestions: Boolean = false
        private set
    var successfulHits: Int = 0
        private set

    fun argueAboutMeaningOfLife() {
        manifestPhysically()
        meaningOfLifeArguments++
        hasResolvedAllQuestions = false
    }

    fun resolveAllQuestionsOnceAndForAll() {
        manifestPhysically()
        meaningOfLifeArguments = 0
        hasResolvedAllQuestions = true
    }

    fun playBrockianUltraCricket(target: Hittable) {
        manifestPhysically()
        successfulHits++
        awardPoint()
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
