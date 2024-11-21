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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess
import kotlin.time.measureTime

object Settings {
    var chats = HashSet<Chat>()
    lateinit var configuration: Configuration
}

private val json = Json {
    prettyPrint = true
}

fun main() {
    println("Init...")
    Settings.configuration = if (Files.configuration.exists()) {
        Yaml.default.decodeFromString(
            Configuration.serializer(),
            Files.configuration.readText()
        )
    } else {
        println("Configuration file not found")
        exitProcess(1)
    }
    println("Configuration loaded...")

    println("Loading chats configs...")
    Settings.chats = json.decodeFromString(Files.chats.readText())
    println("Chats configs loaded for ${Settings.chats.size} chats...")

    val logger = Logger(
        Settings.configuration.influxDb
    )

    val spamFilter = RussianSpamFilter()

    val bot = bot {
        token = Settings.configuration.token
        timeout = Settings.configuration.pollingTimeout
        logLevel = LogLevel.Error

        dispatch {
            text {
                measureTime {
                    val chatId = message.chat.id
                    val chatConfiguration = Settings.chats.firstOrNull { it.id == chatId }
                    if (chatConfiguration != null && chatConfiguration.enabled) {
                        val verifiedUsers: Set<Long> = Json.decodeFromString(
                            Files.verifiedUsers(message.chat.id).readText()
                        )

                        val userId = message.from?.id
                        val messageId = message.messageId
                        val text = text

                        if (!verifiedUsers.contains(message.from?.id)
                            && spamFilter.isSpam(text)
                        ) {
                            logger.detectedSpamMessage(
                                chatId = message.chat.id,
                                message = text
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
                        } else {
                            logger.receivedMessage(
                                message.chat.id,
                                text
                            )
                        }
                    }
                }.let {
                    if(Settings.configuration.debug) {
                        println("Message time processing: $it")
                    }
                }
            }

            command("report_spam") {
                if (messageFromAdmin()) {
                    val replyToMessage = message.replyToMessage
                    if (replyToMessage != null) {
                        logger.reportedSpamMessage(
                            chatId = message.chat.id,
                            message = replyToMessage.text.orEmpty()
                        )
                        bot.deleteMessage(ChatId.fromId(message.chat.id), replyToMessage.messageId)
                        replyToMessage.from?.id?.let { userId ->
                            bot.banChatMember(ChatId.fromId(message.chat.id), userId)
                        }
                    }
                }
                bot.deleteMessage(ChatId.fromId(message.chat.id), message.messageId)
            }

            command("verify_user") {
                if (messageFromAdmin()) {
                    Settings.chats
                        .filter { it.adminChatId == message.chat.id }
                        .forEach { chat ->
                            if (chat.adminChatId != null) {
                                args.getOrNull(0)?.toLong()?.let { userId ->
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

    Files.chats.writeText(
        json.encodeToString(
            ListSerializer(Chat.serializer()),
            Settings.chats.toList()
        )
    )
}
