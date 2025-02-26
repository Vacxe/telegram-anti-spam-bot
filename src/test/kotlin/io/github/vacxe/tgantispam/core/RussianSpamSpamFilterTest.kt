package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.core.filters.SpamFilter
import io.github.vacxe.tgantispam.core.filters.TestSpamFilter
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.assertTrue

class RussianSpamSpamFilterTest {

    private val filter = TestSpamFilter()

    @ParameterizedTest(name = "{index} => {0}")
    @CsvFileSource(resources = [
        "/spam_messages.csv",
    ])
    fun detectSpamTest(message: String) {
        val result = filter.validate(message).maxBy { it.weight }
        assertTrue(
            result is SpamFilter.Result.Quarantine ||
                    result is SpamFilter.Result.Ban
        )
    }
}
