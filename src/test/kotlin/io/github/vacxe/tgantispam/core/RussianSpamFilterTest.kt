package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.assertFalse

class RussianSpamFilterTest {

    private val filter = RussianSpamFilter()

    @ParameterizedTest(name = "{index} => Message: {0}")
    @CsvFileSource(resources = ["/spam_messages.csv"],)
    fun spamTest(message: String) {
        val passed = filter.filter(message)
        assertFalse(passed)
    }
}
