package juko.dto.creatures

class MicePlayer(
    name: String,
    score: Int,
) : Mice(name, score), Playable
