package io.github.vacxe.tgantispam.core.filters

interface SpamFilter {
    fun validate(input: String): Decision

    sealed class Decision(val weight: Int, open val message: String) {
        data class Pass(override val message: String = "Passed") : Decision(0, message)
        data class Quarantine(override val message: String) : Decision(1, message)
        data class Ban(override val message: String) : Decision(Int.MAX_VALUE, message)
    }
}
