package io.github.vacxe.tgantispam.core.data

data class Chat(
    val id: Long,
    val enabled: Boolean = false,
    val adminChatId: Long? = null,
)
