package io.github.vacxe.tgantispam.core.filters

class LanguageInjectionFilter(private val strictLanguage: Regex) : SpamFilter {
    override fun isSpam(input: String): Boolean = input
        .replace(Regex("[!-/]|[:-@]|[\\[-`]|[{-~]\\."), " ") // Remove special symbols
        .replace("\n", " ")
        .replace(Regex("\\s{2,}"), " ")
        .split(" ")
        .any { word ->
            val strictLanguageChars = strictLanguage.findAll(word).toList().size
            val isSpam = strictLanguageChars != 0 && strictLanguageChars != word.length
            if (isSpam) {
                val injectedSymbols = strictLanguage.replace(word, "")
                println("Detected multi language in: \"$word\" Injected symbols: $injectedSymbols")
            }
            isSpam
        }
}
