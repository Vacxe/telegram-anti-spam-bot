package io.github.vacxe.tgantispam.core.filters

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse


class LanguageInjectionFilterTest {
    private val filter = LanguageInjectionFilter(Regex("[А-Яа-яЁё]"))

    @Test
    fun testAllRussian(){
        assertTrue(filter.validate("АБВГДЕЁЖЗИйКЛМНУФХЦЧЩЫЪЬЭЮЯабвгдеёжзийклмнопрстуфхцчщыьъэюя") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testAllRussianWithNumbers(){
        assertTrue(filter.validate("2ая улица") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testAllRussianWithNumbersAndSpecials(){
        assertTrue(filter.validate("2-ая улица") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testAllEnglish(){
        assertTrue(filter.validate("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testRussianWithDot(){
        assertTrue(filter.validate("Сотрудничество\n Опыт.") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testRussianWithDash(){
        assertTrue(filter.validate("Ui-тесты") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testNumbersInNonRussian(){
        assertTrue(filter.validate("По-моему Руслан говорил про это в этом интервью https://youtu.be/GppTWKwEYwE?si=rTvYGlzPStg9jZG_") is SpamFilter.Decision.Pass)
    }

    @Test
    fun testMixed(){
        assertTrue(filter.validate("RБA") is SpamFilter.Decision.Quarantine)
    }
}