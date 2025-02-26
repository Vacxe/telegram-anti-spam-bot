package io.github.vacxe.tgantispam.core.linguistic

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoveUnicodeTransformerTest {

    @Test
    fun testUnicodeRemoved() {
        val input = "\uD83D\uDCBCВозможность дополнительного заработка\uD83D\uDCF2⏳"

        val output = RemoveUnicodeTransformer.transform(input)
        assertEquals("Возможность дополнительного заработка", output)
    }

    @Test
    fun testDollarNotRemoved() {
        val input = "Symbol $ not removed"

        val output = RemoveUnicodeTransformer.transform(input)
        assertEquals("Symbol $ not removed", output)
    }

    @Test
    fun testPlusNotRemoved() {
        val input = "Symbol + not removed"

        val output = RemoveUnicodeTransformer.transform(input)
        assertEquals("Symbol + not removed", output)
    }
}