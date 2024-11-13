package io.github.vacxe.tgantispam.core.linguistic

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoveUnicodeTransformerTest {

    //@Test
    fun test() {
        val input = "\uD83D\uDCBC Возможность дополнительного заработка \n\uD83D\uDCF2 Достаточно смартфона или ноутбука ⏳ Всего 1-2 часа в день\n\n\uD83D\uDC64 Подробности в личку! Есть предложение по oнлайн проeкту. С дневной выручкой от 400 USD. Желающих жду в частной перeписке."

        val output = RemoveUnicodeTransformer().transform(input)
        assertEquals("S", output)
    }
}