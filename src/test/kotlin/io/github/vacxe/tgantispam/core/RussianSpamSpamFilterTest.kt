package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter
import io.github.vacxe.tgantispam.core.filters.SpamFilter
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.assertTrue

class RussianSpamSpamFilterTest {

    private val filter = RussianSpamFilter()

    @ParameterizedTest(name = "{index} => {0}")
    @CsvFileSource(resources = ["/spam_messages.csv"])
    fun detectSpamTest(message: String) {
        val result = filter.validate(message).maxBy { it.weight }
        assertTrue(result is SpamFilter.Decision.Quarantine)
    }
}
