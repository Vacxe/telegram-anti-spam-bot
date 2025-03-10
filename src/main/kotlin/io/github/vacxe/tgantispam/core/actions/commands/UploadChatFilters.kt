package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.document
import com.github.kotlintelegrambot.entities.ChatId
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.messageFromAdmin

object UploadChatFilters {
    fun Dispatcher.uploadChatFilters() {
        apply {
            document {
                if (messageFromAdmin()) {
                    Settings.chats
                        .filter { it.adminChatId == message.chat.id }
                        .forEach { chat ->
                            if (media.fileName == "filters.yaml") {
                                try {
                                    val fileByteArray = bot.downloadFileBytes(media.fileId)

                                    val file = Files.chatFiltersFile(chat.id)
                                    if (!file.exists()) {
                                        file.getParentFile().mkdirs()
                                        file.createNewFile()
                                    }
                                    fileByteArray?.let { file.writeBytes(it) }

                                    Settings.spamFilters[chat.id] = Files.chatFilters(chat.id)
                                } catch (e : Exception) {
                                    bot.sendMessage(ChatId.fromId(message.chat.id), "Config not applied: ${e.message}")
                                } finally {
                                    bot.sendMessage(ChatId.fromId(message.chat.id), "Config successfully applied to chat ${chat.id}")
                                }
                            }
                        }
                }
            }
        }
    }
}
