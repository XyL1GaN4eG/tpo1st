package juko

import kotlin.math.atan

fun main() {
    val math = MyMath()
    for (i in 0..10) println(math.myArctg(i / 10.toDouble()))
}

class MyMath() {
    fun myArctg(number: Double) =
        atan(number)
}