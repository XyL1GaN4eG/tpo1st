package juko.game

import juko.dto.Playable

abstract class Cricket<P : Playable, T : Hittable>(
    createPlayersStore: () -> MutableCollection<P>,
) : Game<P>(createPlayersStore), BattingGame<P, T> {
    abstract fun chooseTarget(targets: Collection<T>): T
}
