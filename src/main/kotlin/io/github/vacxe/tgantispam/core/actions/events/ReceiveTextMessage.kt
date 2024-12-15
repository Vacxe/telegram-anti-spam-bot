package io.github.vacxe.tgantispam.core.actions.events

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.data.Chat
import io.github.vacxe.tgantispam.core.delete
import io.github.vacxe.tgantispam.core.filters.CombineFilter
import io.github.vacxe.tgantispam.core.filters.SpamFilter
import io.github.vacxe.tgantispam.core.logic.IdManager
import io.github.vacxe.tgantispam.core.messageFromAdmin
import kotlinx.serialization.encodeToString
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

                                is SpamFilter.Result.Pass -> {
                                    Settings.goodBehaviourManager.receiveMessage(message)
                                    logger.receivedMessage(
                                        message.chat.id,
                                        text
                                    )
                                }

                                null -> {}
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

        callbackQuery("banButton") {
            callbackQuery.message?.let { message ->
                val userId = message.replyToMessage?.forwardFrom?.id ?: IdManager.getUserIdFromText(message.text)
                ?: return@callbackQuery
                val chatId = message.replyToMessage?.forwardFromChat?.id ?: IdManager.getChatIdFromText(message.text)
                ?: return@callbackQuery
                bot.banChatMember(ChatId.fromId(chatId), userId)
                message.delete(bot)
                message.replyToMessage?.delete(bot)
                println("Action (Ban): $userId in $chatId")
            }
        }

        callbackQuery("approveButton") {
            callbackQuery.message?.let { message ->
                val userId = message.replyToMessage?.forwardFrom?.id ?: IdManager.getUserIdFromText(message.text)
                ?: return@callbackQuery
                val chatId = message.replyToMessage?.forwardFromChat?.id ?: IdManager.getChatIdFromText(message.text)
                ?: return@callbackQuery

                val verifiedUsersFile = Files.verifiedUsers(chatId)
                val verifiedUsers: HashSet<Long> = Json
                    .decodeFromString<Set<Long>>(verifiedUsersFile.readText())
                    .toHashSet()
                verifiedUsers.add(userId)
                verifiedUsersFile.writeText(
                    Settings.json.encodeToString(verifiedUsers)
                )
                message.delete(bot)
                message.replyToMessage?.delete(bot)
                println("Action (Approve): $userId in $chatId")
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

            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(InlineKeyboardButton.CallbackData(text = "Ban", callbackData = "banButton")),
                listOf(InlineKeyboardButton.CallbackData(text = "Approve", callbackData = "approveButton")),
            )

            bot.sendMessage(
                ChatId.fromId(chatConfiguration.adminChatId),
                text = resultMessage("Quarantined", message, reasons),
                replyToMessageId = forwardedMessage.get().messageId,
                disableNotification = true,
                replyMarkup = inlineKeyboardMarkup
            )
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
                    text = resultMessage("Banned", message, reasons),
                    replyToMessageId = forwardedMessage.get().messageId,
                    disableNotification = true
                )
            }
        }

        bot.deleteMessage(ChatId.fromId(message.chat.id), message.messageId)
    }
}

private fun resultMessage(
    action: String,
    message: Message,
    reasons: List<SpamFilter.Result>,
): String = StringBuilder()
    .appendLine("Action: $action")
    .appendLine("${IdManager.USER_ID_PREFIX}${message.from?.id}")
    .appendLine("${IdManager.CHAT_ID_PREFIX}${message.chat.id}")
    .appendLine("---")
    .append(reasons.joinToString("\n") { it.message })
    .toString()