package io.github.vacxe.tgantispam.core.filters

interface SpamFilter {
    fun validate(input: String): Decision

    sealed class Decision(val weight: Int, open val message: String) {
        data object Pass : Decision(0, "All good")
        data class Quarantine(override val message: String) : Decision(1, message)
        data class Ban(override val message: String) : Decision(Int.MAX_VALUE, message)
    }
}
