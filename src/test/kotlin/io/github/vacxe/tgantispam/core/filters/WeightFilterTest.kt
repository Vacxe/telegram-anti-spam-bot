package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer
import io.github.vacxe.tgantispam.core.linguistic.RemoveUnicodeTransformer
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class WeightFilterTest {

    @Test
    fun testDollar() {
       val filter = WeightFilter(
            quarantineWeight = 1,
            //banWeight = 5,
            restrictions = setOf("[$]").map { Regex(it) }.toSet(),
            inputTransformer = CombineTransformer(
                LowercaseTransformer(),
            )
        )
        assertTrue(filter.validate("Test no dollar") is SpamFilter.Result.Pass)
        assertTrue(filter.validate("Test $ dollars") is SpamFilter.Result.Quarantine)
    }


    @Test
    fun testUnicode() {
        val filter = WeightFilter(
            name = "Emoji Limit",
            restrictions = setOf(
                Regex("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s[!-/][:-@][\\[-`][{-~]]")
            ),
            quarantineWeight = 5,
            banWeight = 10)

        assertTrue(filter.validate("Test Two Emoji ✅✅") is SpamFilter.Result.Pass)
        assertTrue(filter.validate("✅ 🔤🔤🔤 🔤🔤🔤 ✅") is SpamFilter.Result.Quarantine)
        assertTrue(filter.validate("✅ 🔤🔤🔤🔤 🔤🔤🔤🔤🔤🔤 ✅ 🔠🔠🔠🔠🔠 🔠 🔠🔠🔠🔠🔠🔠🔠 🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥 🔥🔥🔥🔥🔥 ♦️🔺🔺♦️♦️ ♦️🔺🔺🔺 4️⃣2️⃣5️⃣💲 🔡 🔡🔡🔡🔡 ⚪️⚪️🔴⚪️⚪️🔴 🔴⚪️⚪️ 🔤🔤🔤🔤🔤🔤 🔤 🔤🔤") is SpamFilter.Result.Ban)
    }
}