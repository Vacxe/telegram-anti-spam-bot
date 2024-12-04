package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

abstract class BaseSpamFilter(
    private val name: String? = null,
    private val quarantineWeight: Int = 1,
    private val banWeight: Int = Int.MAX_VALUE,
    protected val inputTransformer: Transformer = PassTransformer()
) : SpamFilter {

    override fun validate(input: String): SpamFilter.Result = validateInput(inputTransformer.transform(input))
    abstract fun validateInput(input: String): SpamFilter.Result
    fun report(
        weight: Int,
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
            val resultMessage = "$filterName Passed"
            println(resultMessage)
            SpamFilter.Result.Pass(resultMessage)
        }
    }
}
