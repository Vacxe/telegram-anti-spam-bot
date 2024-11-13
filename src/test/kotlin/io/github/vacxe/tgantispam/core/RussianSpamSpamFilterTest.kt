package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RussianSpamSpamFilterTest {

    private val filter = RussianSpamFilter()

    @ParameterizedTest(name = "{index} => {0}")
    @CsvFileSource(resources = ["/spam_messages.csv"],)
    fun detectSpamTest(message: String) {
        assertTrue(filter.isSpam(message))
    }

    //@ParameterizedTest(name = "{index} => Message: {0}")
    //@CsvFileSource(resources = ["/spam_messages.csv"],)
    fun regex(message: String) {
        message.lowercase().toCharArray().forEach {
            val text = Regex("[!-/]|[:-@]|[\\[-`]|[{-~]|\\s|\\d|[а-яА-яёЁ]|[a-zA-z]").containsMatchIn("$it")
            val emoji = Regex("(\\u00a9|\\u00ae|[\\u2000-\\u3300]|\\ud83c[\\ud000-\\udfff]|\\ud83d[\\ud000-\\udfff]|\\ud83e[\\ud000-\\udfff])").containsMatchIn("$it")
            assert(text || emoji)
        }
    }
}
