package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.messageFromAdmin

object UnbanUser {
    fun Dispatcher.unbanUser() {
        apply {
            command("unban_user") {
                if (messageFromAdmin()) {
                    val userId = args.getOrNull(0)?.toLong()
                        ?: try {
                            message.replyToMessage?.text?.toLong()
                        } catch (_: Exception) {
                            null
                        }

                    Settings.chats
                        .filter { it.adminChatId == message.chat.id }
                        .forEach { chat ->
                            if (userId != null && chat.adminChatId != null) {
                                bot.unbanChatMember(chatId = ChatId.fromId(chat.id), userId = userId)
                                bot.sendMessage(
                                    ChatId.fromId(chat.adminChatId),
                                    text = "UserId: $userId unbanned in ${chat.id}",
                                    disableNotification = true
                                )
                            }
                        }
                }
            }
        }
    }
}