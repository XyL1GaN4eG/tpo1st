package juko.dto.creatures

open class Mice(
    name: String,
    override val score: Number,
) : Creature(
    name,
), Playable {

}