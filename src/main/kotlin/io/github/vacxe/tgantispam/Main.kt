package io.github.vacxe.tgantispam

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter

object Settings {
    val token = "SET_HERE"
    val adminChatId: Long? = null
    val observeChatIds = setOf<Long>()
}

fun main() {
    val spamFilter = RussianSpamFilter()

    val bot = bot {
        token = Settings.token
        timeout = 10
        logLevel = LogLevel.Error

        dispatch {
            text {
                val chatId = message.chat.id
                if (Settings.observeChatIds.contains(chatId)) {

                    val username = message.from?.username
                    val userId = message.from?.id
                    val firstName = message.from?.firstName
                    val lastName = message.from?.lastName
                    val messageId = message.messageId
                    val text = text

                    println("---")
                    print("SenderID: $userId")
                    firstName?.let { print("$it ") }
                    lastName?.let { print("$it ") }
                    username?.let { println("($it)") }
                    println("ChatId: $chatId")
                    println()
                    println(text)

                    if (!spamFilter.filter(text)) {
                        if(Settings.adminChatId != null) {
                            bot.forwardMessage(
                                ChatId.fromId(Settings.adminChatId),
                                ChatId.fromId(chatId),
                                messageId,
                                protectContent = false,
                                disableNotification = true
                            )

                            bot.sendMessage(
                                ChatId.fromId(Settings.adminChatId),
                                text = "UserId: $userId",
                                disableNotification = true
                            )
                        }
                        bot.deleteMessage(ChatId.fromId(chatId), messageId)
                    }
                }
            }
        }
    }

    bot.startPolling()
}
