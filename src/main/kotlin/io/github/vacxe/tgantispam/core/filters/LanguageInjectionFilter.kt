package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("language_injection")
data class LanguageInjectionFilter(
    private val strictLanguagePattern: String,
    override val name: String? = null,
    override val quarantineWeight: Double = 1.0,
    override val banWeight: Double = Double.MAX_VALUE,
    override val inputTransformer: Transformer = PassTransformer,
) : BaseSpamFilter() {

    override fun validateInput(input: String): SpamFilter.Result {
        val scanResult = input
            .replace(Regex("[!-/]|[:-@]|[\\[-`]|[{-~]\\."), " ") // Remove special symbols
            .replace("\n", " ")
            .replace(Regex("\\s{2,}"), " ")
            .split(" ")
            .mapNotNull { word ->
                val numbers = Regex("\\d").findAll(word).toList().size
                val strictLanguageChars = Regex(strictLanguagePattern).findAll(word).toList().size
                val isSpam = strictLanguageChars != 0 && strictLanguageChars + numbers != word.length
                if (isSpam) {
                    val injectedSymbols = Regex(strictLanguagePattern).replace(word, "")
                    "\"$word\":\"$injectedSymbols\""
                } else
                    null
            }
        val weight = scanResult.size
        val message = if (scanResult.isNotEmpty())
            "Found injection in: ${scanResult.joinToString(", ")}"
        else "No Injection"

        return report(weight.toDouble(), message)
    }
}
