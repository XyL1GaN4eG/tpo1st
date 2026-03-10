package juko.dto

class MicePlayer(
    name: String,
    score: Int,
) : Mice(name, score), Playable {
    fun enterMatch() {
        TODO("Not yet implemented")
    }
}
