package juko.game

import juko.dto.Playable

abstract class Game<T : Playable>(
    createPlayersStore: () -> MutableCollection<T>,
) {
    private val playersStore: MutableCollection<T> = createPlayersStore()
    val players: Collection<T>
        get() = playersStore.toList()

    open fun addPlayer(player: T): Boolean {
        if (player in playersStore) return false
        return playersStore.add(player)
    }

    open fun removePlayer(player: T): Boolean {
        return playersStore.remove(player)
    }

    protected fun hasPlayer(player: T): Boolean = player in playersStore

    protected fun requeuePlayer(
        player: T,
        update: (T) -> Unit = {},
    ) {
        check(playersStore.remove(player)) { "Player must be registered before score update" }
        update(player)
        playersStore.add(player)
    }

    protected fun highestScoringPlayer(): T? = playersStore.maxByOrNull { it.score }

    protected fun snapshotPlayers(): List<T> = playersStore.toList()

    abstract fun start()

    abstract fun finish()

    abstract fun result()

    abstract fun resultByPlayer(player: T)
}
