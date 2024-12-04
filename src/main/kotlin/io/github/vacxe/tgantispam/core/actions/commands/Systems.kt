package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.messageFromAdmin
import io.github.vacxe.tgantispam.core.updateChatConfig

object Systems {
    fun Dispatcher.systems() {
        apply {
            command("get_chat_id") {
                if (messageFromAdmin()) {
                    bot.sendMessage(ChatId.fromId(message.chat.id), message.chat.id.toString())
                }
            }

            command("enable") {
                if (messageFromAdmin()) {
                    updateChatConfig { chat, _ ->
                        chat.copy(enabled = true)
                    }
                }
            }

            command("disable") {
                if (messageFromAdmin()) {
                    updateChatConfig { chat, _ ->
                        chat.copy(enabled = false)
                    }
                }
            }

            command("set_admin_chat_id") {
                if (messageFromAdmin()) {
                    updateChatConfig { chat, _ ->
                        chat.copy(adminChatId = args.getOrNull(0)?.toLong())
                    }
                }
            }

            command("set_login_captcha") {
                if (messageFromAdmin()) {

                }
            }

            command("dump_logs") {
                if (messageFromAdmin()) {
                    Settings.chats.firstOrNull {
                        it.adminChatId == message.chat.id
                    }?.id?.let { chatId ->
                        bot.sendDocument(
                            chatId = ChatId.fromId(chatId),
                            TelegramFile.ByFile(Files.filteredSpamFile(chatId))
                        )
                        bot.sendDocument(
                            chatId = ChatId.fromId(chatId),
                            TelegramFile.ByFile(Files.unfilteredSpamFile(chatId))
                        )
                    }
                }
            }
        }
    }
}