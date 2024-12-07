package io.github.vacxe.tgantispam.core.actions.events

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.data.Chat
import io.github.vacxe.tgantispam.core.filters.CombineFilter
import io.github.vacxe.tgantispam.core.filters.SpamFilter
import io.github.vacxe.tgantispam.core.logic.GoodBehaviourManager
import io.github.vacxe.tgantispam.core.messageFromAdmin
import kotlinx.serialization.json.Json
import kotlin.time.measureTime

object ReceiveTextMessage {
    fun Dispatcher.receiveTextMessage(
        spamFilters: HashMap<Long, CombineFilter>,
        logger: Logger
    ) {
        apply {
            text {
                measureTime {
                    val chatId = message.chat.id
                    val chatConfiguration = Settings.chats.firstOrNull { it.id == chatId }
                    if (chatConfiguration != null && chatConfiguration.enabled) {
                        val verifiedUsers: Set<Long> = Json.decodeFromString(
                            Files.verifiedUsers(message.chat.id).readText()
                        )

                        if (!verifiedUsers.contains(message.from?.id) && !messageFromAdmin()) {
                            val spamFilter = spamFilters.getOrDefault(chatId, CombineFilter())
                            val results = spamFilter.validate(text)
                            when (results.maxByOrNull { it.weight }) {
                                is SpamFilter.Result.Quarantine -> proceedQuarantine(
                                    this,
                                    logger,
                                    chatConfiguration,
                                    message,
                                    results
                                )

                                is SpamFilter.Result.Ban -> proceedBan(
                                    this,
                                    logger,
                                    chatConfiguration,
                                    message,
                                    results
                                )

                                is SpamFilter.Result.Pass  -> {
                                    Settings.goodBehaviourManager.receiveMessage(message)
                                    logger.receivedMessage(
                                        message.chat.id,
                                        text
                                    )
                                }

                                null -> { }
                            }
                        }

                    } else {
                        logger.receivedMessage(
                            message.chat.id,
                            text
                        )
                    }
                }.let {
                    if (Settings.configuration.debug) {
                        println("Message time processing: $it")
                    }
                }
            }
        }
    }
}

fun proceedQuarantine(
    textHandlerEnvironment: TextHandlerEnvironment,
    logger: Logger,
    chatConfiguration: Chat,
    message: Message,
    reasons: List<SpamFilter.Result>
) {
    textHandlerEnvironment.apply {
        logger.detectedSpamMessage(
            chatId = message.chat.id,
            message = text
        )
        if (chatConfiguration.adminChatId != null) {
            val forwardedMessage = bot.forwardMessage(
                ChatId.fromId(chatConfiguration.adminChatId),
                ChatId.fromId(message.chat.id),
                message.messageId,
                protectContent = false,
                disableNotification = true
            )

            bot.sendMessage(
                ChatId.fromId(chatConfiguration.adminChatId),
                text = "${message.from?.id}",
                replyToMessageId = forwardedMessage.get().messageId,
                disableNotification = true,
            )

            if (Settings.configuration.debug) {
                bot.sendMessage(
                    ChatId.fromId(chatConfiguration.adminChatId),
                    text = reasons.joinToString("\n") { it.message },
                    disableNotification = true
                )
            }
        }
        bot.deleteMessage(ChatId.fromId(message.chat.id), message.messageId)
    }
}

fun proceedBan(
    textHandlerEnvironment: TextHandlerEnvironment,
    logger: Logger,
    chatConfiguration: Chat,
    message: Message,
    reasons: List<SpamFilter.Result>
) {
    textHandlerEnvironment.apply {
        logger.detectedSpamMessage(
            chatId = message.chat.id,
            message = text
        )
        message.from?.id?.let { userId ->
            bot.banChatMember(ChatId.fromId(message.chat.id), userId)
            if (chatConfiguration.adminChatId != null) {
                val forwardedMessage = bot.forwardMessage(
                    ChatId.fromId(chatConfiguration.adminChatId),
                    ChatId.fromId(message.chat.id),
                    message.messageId,
                    protectContent = false,
                    disableNotification = true
                )
                bot.sendMessage(
                    ChatId.fromId(chatConfiguration.adminChatId),
                    text = "UserId: $userId **Auto Banned** in ${message.chat.id}]",
                    replyToMessageId = forwardedMessage.get().messageId,
                    disableNotification = true
                )
                bot.sendMessage(
                    ChatId.fromId(chatConfiguration.adminChatId),
                    text = reasons.joinToString("\n") { it.message },
                    replyToMessageId = forwardedMessage.get().messageId,
                    disableNotification = true
                )
            }
        }

        bot.deleteMessage(ChatId.fromId(message.chat.id), message.messageId)
    }
}