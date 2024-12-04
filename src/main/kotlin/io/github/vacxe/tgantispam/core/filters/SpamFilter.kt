package io.github.vacxe.tgantispam.core.filters

interface SpamFilter {
    fun validate(input: String): Result

    sealed class Result(val weight: Int, open val message: String) {
        data class Pass(override val message: String = "Passed") : Result(0, message)
        data class Quarantine(override val message: String) : Result(1, message)
        data class Ban(override val message: String) : Result(Int.MAX_VALUE, message)
    }
}
