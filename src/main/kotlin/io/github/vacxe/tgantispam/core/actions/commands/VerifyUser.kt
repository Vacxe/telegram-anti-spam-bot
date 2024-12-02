package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.messageFromAdmin
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object VerifyUser {
    fun apply(
        dispatcher: Dispatcher,
        json: Json,
    ) {
        dispatcher.apply {
            command("verify_user") {
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
                            if (chat.adminChatId != null) {
                                if (userId != null) {
                                    val verifiedUsersFile = Files.verifiedUsers(chat.id)
                                    val verifiedUsers: HashSet<Long> = Json
                                        .decodeFromString<Set<Long>>(verifiedUsersFile.readText())
                                        .toHashSet()
                                    verifiedUsers.add(userId)
                                    verifiedUsersFile.writeText(
                                        json.encodeToString(verifiedUsers)
                                    )
                                    bot.sendMessage(
                                        ChatId.fromId(chat.adminChatId),
                                        text = "UserId: $userId verified in ${chat.id}",
                                        disableNotification = true
                                    )
                                }
                            }
                        }
                }
            }
        }
    }
}