package juko.dto

abstract class Creature(
    val name: String,
) {
    open fun manifestPhysically() {
        // no-op stub for domain model tests
    }
}
