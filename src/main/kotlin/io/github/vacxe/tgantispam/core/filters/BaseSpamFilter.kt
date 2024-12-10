package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

abstract class BaseSpamFilter(
    private val name: String? = null,
    private val quarantineWeight: Double = 1.0,
    private val banWeight: Double = Double.MAX_VALUE,
    private val inputTransformer: Transformer = PassTransformer()
) : SpamFilter {

    override fun validate(input: String): SpamFilter.Result = validateInput(inputTransformer.transform(input))
    abstract fun validateInput(input: String): SpamFilter.Result
    fun report(
        weight: Double,
        message: String
    ): SpamFilter.Result {
        val filterName = name ?: "Filter"
        return if (weight >= banWeight) {
            val resultMessage = "$filterName: Ban -> $message"
            println(resultMessage)
            SpamFilter.Result.Ban(resultMessage)
        } else if (weight >= quarantineWeight) {
            val resultMessage = "$filterName: Quarantine -> $message"
            println(resultMessage)
            SpamFilter.Result.Quarantine(resultMessage)
        } else {
            val resultMessage = "$filterName Passed -> $message"
            println(resultMessage)
            SpamFilter.Result.Pass(resultMessage)
        }
    }
}
