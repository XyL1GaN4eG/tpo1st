package juko.dto

abstract class Creature(
    val name: String,
) {
    var isPhysicallyManifested: Boolean = false
        private set

    open fun manifestPhysically() {
        isPhysicallyManifested = true
    }
}
