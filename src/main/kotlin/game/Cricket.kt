package juko.game

import juko.dto.Playable

abstract class Cricket<player : Playable, target : Hittable> : Game<player>(), BattingGame {
    abstract fun chooseTarget(targets: Collection<target>): target
}
