package com.atek.scs.feature.common.network.model


data class SpecialFareModes(
    var entryExitOverride: Boolean = false,
    var excessTimeOverride: Boolean = false,
    var excessFareOverride: Boolean = false,
    var fareOverrideOne: Boolean = false,
    var fareOverrideTwo: Boolean = false
) {
    /**
     * A computed property that returns all modes and their values as a map.
     * This is the single source of truth and is more efficient for UI iteration.
     */
    val modesAsMap: Map<String, Boolean>
        get() = mapOf(
            "ENTRY EXIT OVERRIDE" to entryExitOverride,
            "EXCESS TIME OVERRIDE" to excessTimeOverride,
            "EXCESS FARE OVERRIDE" to excessFareOverride,
            "FARE OVERRIDE ONE" to fareOverrideOne,
            "FARE OVERRIDE TWO" to fareOverrideTwo
        )
}