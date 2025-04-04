package io.github.vacxe.tgantispam.core.logic

import kotlin.test.Test

class IdManagerTest {

    @Test
    fun getUserIdTest() {
        val input = StringBuilder()
            .appendLine("Action: Ban")
            .appendLine("User ID: 123")
            .appendLine("Chat ID: -321")
            .appendLine("---")
            .toString()

        val result = IdManager.getUserIdFromText(input)
        kotlin.test.assertEquals(123L, result)
    }

    @Test
    fun getUserIdIsNullTest() {
        val input = StringBuilder()
            .appendLine("Action: Ban")
            .appendLine("User ID: null")
            .appendLine("Chat ID: -321")
            .appendLine("---")
            .toString()

        val result = IdManager.getUserIdFromText(input)
        kotlin.test.assertEquals(null, result)
    }

    @Test
    fun getUserIdIsNotANumberTest() {
        val input = StringBuilder()
            .appendLine("Action: Ban")
            .appendLine("User ID: foo")
            .appendLine("Chat ID: -321")
            .appendLine("---")
            .toString()

        val result = IdManager.getUserIdFromText(input)
        kotlin.test.assertEquals(null, result)
    }

    @Test
    fun getUserIdFromEmptyMessageTest() {
        val input = String()
        val result = IdManager.getUserIdFromText(input)
        kotlin.test.assertEquals(null, result)
    }

    @Test
    fun getChatIdNegativeTest() {
        val input = StringBuilder()
            .appendLine("Action: Ban")
            .appendLine("User ID: foo")
            .appendLine("Chat ID: -321")
            .appendLine("---")
            .toString()

        val result = IdManager.getChatIdFromText(input)
        kotlin.test.assertEquals(-321, result)
    }

    @Test
    fun getChatIdPositiveTest() {
        val input = StringBuilder()
            .appendLine("Action: Ban")
            .appendLine("User ID: foo")
            .appendLine("Chat ID: 321")
            .appendLine("---")
            .toString()

        val result = IdManager.getChatIdFromText(input)
        kotlin.test.assertEquals(321, result)
    }
}
