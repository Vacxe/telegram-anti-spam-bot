package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.messageFromAdmin

object BanUser {
    fun Dispatcher.banUser() {
        apply {
            command("ban_user") {
                if (messageFromAdmin()) {
                    try {
                        val userId = message.replyToMessage?.forwardFrom?.id

                        Settings.chats
                            .filter { it.adminChatId == message.chat.id }
                            .forEach { chat ->
                                if (userId != null && chat.adminChatId != null) {
                                    bot.banChatMember(chatId = ChatId.fromId(chat.id), userId = userId)
                                    bot.sendMessage(
                                        ChatId.fromId(chat.adminChatId),
                                        text = "$userId banned",
                                        disableNotification = true
                                    )
                                }
                            }
                    } catch (e: Exception) {
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            text = "${e.message}",
                            disableNotification = true
                        )
                    }
                }
            }
        }
    }
}