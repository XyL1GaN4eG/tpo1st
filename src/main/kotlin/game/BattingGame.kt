package juko.game

import juko.dto.Playable

interface BattingGame<P : Playable, T : Hittable> {
    fun bat(player: P, target: T)
}
