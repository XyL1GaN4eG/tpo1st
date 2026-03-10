package juko.dto

class MicePlayer(
    name: String,
    score: Int,
) : Mice(name, score), Playable {
    fun enterMatch() {
        // no-op stub for domain model tests
    }
}
