package io.github.vacxe.tgantispam.core.logic

object UserIdManager {
    const val USER_ID_PREFIX = "User ID: "
    private val usedIdPattern = Regex("$USER_ID_PREFIX\\d+")

    const val CHAT_ID_PREFIX = "Chat ID: "
    private val chatIdPattern = Regex("$CHAT_ID_PREFIX\\d+")
    fun getUserIdFromText(text: String?) =
        try {
            text?.let { usedIdPattern.find(it)?.value?.replace(USER_ID_PREFIX, "")?.toLong() }
        } catch (_: Exception) {
            null
        }

    fun getChatIdFromText(text: String?) =
        try {
            text?.let { chatIdPattern.find(it)?.value?.replace(CHAT_ID_PREFIX, "")?.toLong() }
        } catch (_: Exception) {
            null
        }
}