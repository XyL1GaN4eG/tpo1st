package juko.dto

open class Mice(
    name: String,
    override val score: Number,
) : Creature(name), Playable, Comparable<Mice> {
    override fun compareTo(other: Mice): Int {
        TODO("Not yet implemented")
    }

}
