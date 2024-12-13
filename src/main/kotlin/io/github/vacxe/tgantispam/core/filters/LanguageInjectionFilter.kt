package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

class LanguageInjectionFilter(
    private val strictLanguage: Regex,
    name: String? = null,
    quarantineWeight: Int = 1,
    banWeight: Int = Int.MAX_VALUE,
    inputTransformer: Transformer = PassTransformer()
) : BaseSpamFilter(
    name,
    quarantineWeight.toDouble(),
    banWeight.toDouble(),
    inputTransformer
) {
    override fun validateInput(input: String): SpamFilter.Result {
        val scanResult = input
            .replace(Regex("[!-/]|[:-@]|[\\[-`]|[{-~]\\."), " ") // Remove special symbols
            .replace("\n", " ")
            .replace(Regex("\\s{2,}"), " ")
            .split(" ")
            .mapNotNull { word ->
                val numbers = Regex("\\d").findAll(word).toList().size
                val strictLanguageChars = strictLanguage.findAll(word).toList().size
                val isSpam = strictLanguageChars != 0 && strictLanguageChars + numbers != word.length
                if (isSpam) {
                    val injectedSymbols = strictLanguage.replace(word, "")
                    "\"$word\":\"$injectedSymbols\""
                } else
                    null
            }
        val weight = scanResult.size
        val message = if(scanResult.isNotEmpty())
            "Found injection in: ${scanResult.joinToString(", ")}"
        else "No Injection"

        return report(weight.toDouble(), message)
    }
}
