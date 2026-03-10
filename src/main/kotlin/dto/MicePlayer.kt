package juko.dto

class MicePlayer(
    name: String,
    score: Int,
) : Mice(name, score), Playable {
    var enteredMatches: Int = 0
        private set
    var isInMatch: Boolean = false
        private set

    fun enterMatch() {
        manifestPhysically()
        enteredMatches++
        isInMatch = true
    }

    fun leaveMatch() {
        isInMatch = false
    }
}
