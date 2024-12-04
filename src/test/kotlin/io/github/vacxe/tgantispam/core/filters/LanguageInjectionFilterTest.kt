package io.github.vacxe.tgantispam.core.filters

import kotlin.test.Test
import kotlin.test.assertTrue


class LanguageInjectionFilterTest {
    private val filter = LanguageInjectionFilter(Regex("[А-Яа-яЁё]"))

    @Test
    fun testAllRussian(){
        assertTrue(filter.validate("АБВГДЕЁЖЗИйКЛМНУФХЦЧЩЫЪЬЭЮЯабвгдеёжзийклмнопрстуфхцчщыьъэюя") is SpamFilter.Result.Pass)
    }

    @Test
    fun testAllRussianWithNumbers(){
        assertTrue(filter.validate("2ая улица") is SpamFilter.Result.Pass)
    }

    @Test
    fun testAllRussianWithNumbersAndSpecials(){
        assertTrue(filter.validate("2-ая улица") is SpamFilter.Result.Pass)
    }

    @Test
    fun testAllEnglish(){
        assertTrue(filter.validate("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM") is SpamFilter.Result.Pass)
    }

    @Test
    fun testRussianWithDot(){
        assertTrue(filter.validate("Сотрудничество\n Опыт.") is SpamFilter.Result.Pass)
    }

    @Test
    fun testRussianWithDash(){
        assertTrue(filter.validate("Ui-тесты") is SpamFilter.Result.Pass)
    }

    @Test
    fun testNumbersInNonRussian(){
        assertTrue(filter.validate("По-моему Руслан говорил про это в этом интервью https://youtu.be/GppTWKwEYwE?si=rTvYGlzPStg9jZG_") is SpamFilter.Result.Pass)
    }

    @Test
    fun testMixed(){
        assertTrue(filter.validate("RБA") is SpamFilter.Result.Quarantine)
    }
}