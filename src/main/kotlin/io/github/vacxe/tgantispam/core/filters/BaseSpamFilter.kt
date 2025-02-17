package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.Transformer
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseSpamFilter : SpamFilter {

    abstract val name: String?
    abstract val quarantineWeight: Double
    abstract val banWeight: Double
    abstract val inputTransformer: Transformer

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
