package juko.game

import juko.dto.Playable

abstract class Game<T : Playable>(
    createPlayersStore: () -> MutableCollection<T>,
) {
    private val playersStore: MutableCollection<T> = createPlayersStore()
    val players: Collection<T>
        get() = playersStore.toList()

    fun addPlayer(player: T): Boolean {
        if (player in playersStore) return false
        return playersStore.add(player)
    }

    fun removePlayer(player: T): Boolean {
        return playersStore.remove(player)
    }

    protected fun hasPlayer(player: T): Boolean = player in playersStore

    protected fun requeuePlayer(player: T) {
        check(removePlayer(player)) { "Player must be registered before score update" }
        addPlayer(player)
    }

    abstract fun start()

    abstract fun finish()

    abstract fun result()

    abstract fun resultByPlayer(player: T)
}
