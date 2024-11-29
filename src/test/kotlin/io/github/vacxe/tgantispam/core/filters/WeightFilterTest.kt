package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer
import io.github.vacxe.tgantispam.core.linguistic.RemoveUnicodeTransformer
import io.github.vacxe.tgantispam.core.linguistic.RussianCharacterTransformer
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class WeightFilterTest {

    @Test
    fun test() {
       val filter = WeightFilter(
            quarantineWeight = 1,
            //banWeight = 5,
            restrictions = setOf("[$]").map { Regex(it) }.toSet(),
            inputTransformer = CombineTransformer(
                RemoveUnicodeTransformer(),
                LowercaseTransformer(),
                RussianCharacterTransformer()
            )
        )
        assertTrue(filter.validate("Test no dollar") is SpamFilter.Decision.Pass)
        assertTrue(filter.validate("Test $ dollars") is SpamFilter.Decision.Quarantine)
    }
}