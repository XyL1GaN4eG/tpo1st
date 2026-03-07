package juko.game

import juko.dto.creatures.Playable

abstract class Cricket<player : Playable, target : Hittable> : Game<player>(), BattingGame
