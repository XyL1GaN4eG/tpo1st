package juko.game

import juko.dto.creatures.Playable
import juko.juko.game.BattingGame

abstract class Cricket<player : Playable, target : Hittable> : Game<player>(), BattingGame
