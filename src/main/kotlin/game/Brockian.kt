package juko.game

import juko.dto.Playable

interface Brockian<P : Playable> {
    fun hitWithoutVisibleReason(player: P, target: Hittable)
}
