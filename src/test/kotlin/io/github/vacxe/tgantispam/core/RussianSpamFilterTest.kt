package io.github.vacxe.tgantispam.core

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertFalse

class RussianSpamFilterTest {

    private val filter = RussianSpamFilter()

    @TestFactory
    fun factory() = listOf(
        "\uD83D\uDCBC Возможность дополнительного заработка \n\uD83D\uDCF2 Достаточно смартфона или ноутбука ⏳ Всего 1-2 часа в день\n\n\uD83D\uDC64 Подробности в личку!",
        "Есть предложение по oнлайн проeкту. С дневной выручкой  от 400 USD. Желающих жду в частной перeписке."
    ).map { message ->
        DynamicTest.dynamicTest(message) {
            assertFalse(filter.filter(message))
        }
    }
}