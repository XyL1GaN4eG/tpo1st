package juko.game

import juko.dto.creatures.Playable

abstract class Game<T : Playable> {
    abstract val players: MutableCollection<T>

    fun addPlayer(player: T) {
        players.add(player)
    }

    fun removePlayer(player: T) {
        players.remove(player)
    }

    abstract fun start()

    abstract fun finish()

    abstract fun result()

    abstract fun resultByPlayer(player: T)
}
