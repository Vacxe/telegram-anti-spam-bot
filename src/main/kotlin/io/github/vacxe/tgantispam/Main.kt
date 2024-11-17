package io.github.vacxe.tgantispam

import com.charleskorn.kaml.Yaml
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.configuration.Configuration
import io.github.vacxe.tgantispam.core.data.Chat
import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter
import kotlin.system.exitProcess

object Settings {
    val chats = hashSetOf(
        Chat(
            id = -1001181570704L,
            enabled = true,
            adminChatId = -1002469955497L
        )
    )
}

fun main() {
    println("Init...")
    val configuration = if(Files.configuration.exists()) {
        Yaml.default.decodeFromString(
            Configuration.serializer(),
            "Files.configuration.readText()"
        )
    } else {
        println("Configuration file not found")
        exitProcess(1)
    }

    println("Configuration loaded...")

    val spamFilter = RussianSpamFilter()
    val logger = Logger()

    val bot = bot {
        token = configuration.token
        timeout = configuration.pollingTimeout
        logLevel = LogLevel.Error

        dispatch {
            text {
                val chatId = message.chat.id
                val chatConfiguration = Settings.chats.firstOrNull { it.id == chatId }
                if (chatConfiguration != null && chatConfiguration.enabled) {
                    val userId = message.from?.id
                    val messageId = message.messageId
                    val text = text

                    if (spamFilter.isSpam(text)) {
                        logger.receivedSpamMessage(
                            message = text,
                            detected = true
                        )
                        if (chatConfiguration.adminChatId != null) {
                            bot.forwardMessage(
                                ChatId.fromId(chatConfiguration.adminChatId),
                                ChatId.fromId(chatId),
                                messageId,
                                protectContent = false,
                                disableNotification = true
                            )

                            bot.sendMessage(
                                ChatId.fromId(chatConfiguration.adminChatId),
                                text = "$userId",
                                disableNotification = true
                            )
                        }
                        bot.deleteMessage(ChatId.fromId(chatId), messageId)
                    }
                }
            }

            command("report_spam") {
                if (messageFromAdmin()) {
                    val replyToMessage = message.replyToMessage
                    if (replyToMessage != null) {
                        logger.receivedSpamMessage(
                            message = replyToMessage.text.orEmpty(),
                            detected = false
                        )
                        bot.deleteMessage(ChatId.fromId(message.chat.id), replyToMessage.messageId)
                        replyToMessage.from?.id?.let { userId ->
                            bot.banChatMember(ChatId.fromId(message.chat.id), userId)
                        }
                    }
                }
                bot.deleteMessage(ChatId.fromId(message.chat.id), message.messageId)
            }

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
                    // TODO
                }
            }

            command("ban") {
                if (messageFromAdmin()) {
                    Settings.chats
                        .filter { it.adminChatId == message.chat.id }
                        .forEach { chat ->
                            if (chat.adminChatId != null) {
                                val userId = args.getOrNull(0)?.toLong()
                                val chatId = args.getOrNull(1)?.toLong()
                                if (userId != null) {
                                    if (chatId != null) {
                                        bot.banChatMember(chatId = ChatId.fromId(chatId), userId = userId)
                                        bot.sendMessage(
                                            ChatId.fromId(chat.adminChatId),
                                            text = "UserId: $userId banned in $chatId",
                                            disableNotification = true
                                        )
                                    } else {
                                        bot.banChatMember(chatId = ChatId.fromId(chat.id), userId = userId)
                                        bot.sendMessage(
                                            ChatId.fromId(chat.adminChatId),
                                            text = "UserId: $userId banned in ${chat.id}",
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

    bot.startPolling()
    println("Bot polling...")
}

fun CommandHandlerEnvironment.messageFromAdmin(): Boolean = bot
    .getChatAdministrators(ChatId.fromId(message.chat.id))
    .getOrNull()
    ?.any { it.user.id == message.from?.id } ?: false

fun CommandHandlerEnvironment.updateChatConfig(update: (Chat, Message) -> Chat) {
    val chatConfig =
        Settings.chats.firstOrNull { it.id == message.chat.id } ?: Chat(id = message.chat.id)
    Settings.chats.remove(chatConfig)
    Settings.chats.add(update.invoke(chatConfig, message))
}
