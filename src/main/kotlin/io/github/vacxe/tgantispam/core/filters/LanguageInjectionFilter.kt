package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

class LanguageInjectionFilter(
    private val strictLanguage: Regex,
    private val quarantineWeight: Int = 1,
    private val banWeight: Int = Int.MAX_VALUE,
    private val inputTransformer: Transformer = PassTransformer()
) : SpamFilter {
    override fun validate(input: String): SpamFilter.Decision {
        val transformedInput = inputTransformer.transform(input)
        val scanResult = transformedInput
            .replace(Regex("[!-/]|[:-@]|[\\[-`]|[{-~]\\."), " ") // Remove special symbols
            .replace("\n", " ")
            .replace(Regex("\\s{2,}"), " ")
            .split(" ")
            .map { word ->
                val numbers = Regex("\\d").findAll(word).toList().size
                val strictLanguageChars = strictLanguage.findAll(word).toList().size
                val isSpam = strictLanguageChars != 0 && strictLanguageChars + numbers != word.length
                return@map if (isSpam) {
                    val injectedSymbols = strictLanguage.replace(word, "")
                    val message = "\"$word\":\"$injectedSymbols\""
                    message
                } else null
            }
            .filterNotNull()
        val size = scanResult.size
        val message = "Detected multi language in: ${scanResult.joinToString(", ")}"

        return if (size >= banWeight) {
            println(message)
            SpamFilter.Decision.Ban(message)
        } else if (size >= quarantineWeight) {
            println(message)
            SpamFilter.Decision.Quarantine(message)
        } else SpamFilter.Decision.Pass
    }
}
