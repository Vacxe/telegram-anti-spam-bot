package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

class LanguageInjectionFilter(
    private val strictLanguage: Regex,
    private val inputTransformer: Transformer = PassTransformer()
) : SpamFilter {
    override fun validate(input: String): SpamFilter.Decision {
        val transformedInput = inputTransformer.transform(input)
        transformedInput
            .replace(Regex("[!-/]|[:-@]|[\\[-`]|[{-~]\\."), " ") // Remove special symbols
            .replace("\n", " ")
            .replace(Regex("\\s{2,}"), " ")
            .split(" ")
            .forEach { word ->
                val numbers = Regex("\\d").findAll(word).toList().size
                val strictLanguageChars = strictLanguage.findAll(word).toList().size
                val isSpam = strictLanguageChars != 0 && strictLanguageChars + numbers != word.length
                if (isSpam) {
                    val injectedSymbols = strictLanguage.replace(word, "")
                    val message = "Detected multi language in: \"$word\" Injected symbols: $injectedSymbols"
                    println(message)
                    return SpamFilter.Decision.Quarantine(message)
                }
            }
        return SpamFilter.Decision.Pass

    }
}
