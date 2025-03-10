package io.github.vacxe.tgantispam.core

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.data.Chat
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

fun TextHandlerEnvironment.messageFromAdmin(): Boolean = bot
    .getChatAdministrators(ChatId.fromId(message.chat.id))
    .getOrNull()
    ?.any { it.user.id == message.from?.id } ?: false

fun CommandHandlerEnvironment.messageFromAdmin(): Boolean = bot
    .getChatAdministrators(ChatId.fromId(message.chat.id))
    .getOrNull()
    ?.any { it.user.id == message.from?.id } ?: false

fun MediaHandlerEnvironment<*>.messageFromAdmin(): Boolean = bot
    .getChatAdministrators(ChatId.fromId(message.chat.id))
    .getOrNull()
    ?.any { it.user.id == message.from?.id } ?: false

private val json = Json {
    prettyPrint = true
}

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

fun Message.delete(bot: Bot) = bot.deleteMessage(ChatId.fromId(this.chat.id), this.messageId)
