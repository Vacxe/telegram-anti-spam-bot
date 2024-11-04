package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RussianSpamFilterTest {

    private val filter = RussianSpamFilter()

    @ParameterizedTest(name = "{index} => Message: {0}")
    @CsvFileSource(resources = ["/spam_messages.csv"],)
    fun spamTest(message: String) {
        val passed = filter.filter(message)
        assertFalse(passed)
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
