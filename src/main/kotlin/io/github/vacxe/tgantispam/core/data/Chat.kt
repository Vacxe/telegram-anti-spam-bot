package io.github.vacxe.tgantispam.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Long,
    val enabled: Boolean = false,
    val adminChatId: Long? = null
)
