package io.github.vacxe.tgantispam.core.filters

class LanguageInjectionFilter(private val strictLanguage: Regex) : SpamFilter {
    override fun isSpam(input: String): Boolean = input
        .replace(Regex("[!-/]|[:-@]|[\\[-`]|[{-~]\\d"), "") // Remove special symbols
        .split(" ")
        .any { word ->
            val strictLanguageChars = strictLanguage.findAll(word).toList().size
            val isSpam = strictLanguageChars != 0 && strictLanguageChars != word.length
            if (isSpam) {
                val injectedSymbols = strictLanguage.replace(word, "").toCharArray()
                println("Detected multi language in: $word. Injected symbols: $injectedSymbols")
            }
            isSpam
        }
}
