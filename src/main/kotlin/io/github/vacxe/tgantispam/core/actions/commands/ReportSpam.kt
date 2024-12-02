package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.messageFromAdmin

object ReportSpam {
    fun apply(
        dispatcher: Dispatcher,
        logger: Logger,
    ) {
        dispatcher.apply {
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
        }
    }
}