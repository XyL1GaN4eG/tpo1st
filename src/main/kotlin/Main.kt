package juko

import juko.mymath.Arctg

fun main() {
    val math = Arctg()
    for (i in 0..10) println(math.myArctg(i / 10.toDouble()))
}
