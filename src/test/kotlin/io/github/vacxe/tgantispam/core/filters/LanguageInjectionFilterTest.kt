package io.github.vacxe.tgantispam.core.filters

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse


class LanguageInjectionFilterTest {
    private val filter = LanguageInjectionFilter(Regex("[А-Яа-яЁё\\d]"))

    @Test
    fun testAllRussian(){
        assertFalse(filter.isSpam("АБВГДЕЁЖЗИйКЛМНУФХЦЧЩЫЪЬЭЮЯабвгдеёжзийклмнопрстуфхцчщыьъэюя"))
    }

    @Test
    fun testAllRussianWithNumbers(){
        assertFalse(filter.isSpam("2ая улица"))
    }

    @Test
    fun testAllRussianWithNumbersAndSpecials(){
        assertFalse(filter.isSpam("2-ая улица"))
    }

    @Test
    fun testAllEnglish(){
        assertFalse(filter.isSpam("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"))
    }

    @Test
    fun testRussianWithDot(){
        assertFalse(filter.isSpam("Сотрудничество\n Опыт."))
    }

    @Test
    fun testRussianWithDash(){
        assertFalse(filter.isSpam("Ui-тесты"))
    }

    @Test
    fun testMixed(){
        assertTrue(filter.isSpam("RБA"))
    }
}