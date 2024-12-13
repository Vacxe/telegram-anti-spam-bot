package io.github.vacxe.tgantispam.core.actions.commands

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.chatMember
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.actions.commands.BanUser.banUser
import io.github.vacxe.tgantispam.core.messageFromAdmin
import io.github.vacxe.tgantispam.core.updateChatConfig

object Captcha {
    fun Dispatcher.captcha() {
        apply {
            chatMember {
                val isMember = this.chatMember.oldChatMember.isMember == false
                val notWasAMember = this.chatMember.newChatMember.isMember == true
                if (isMember && notWasAMember) {
                    val newChatMember = chatMember.newChatMember.user
                    println("UserId:${newChatMember.id}, UserName: ${newChatMember.username} (${newChatMember.firstName} ${newChatMember.lastName}) joined ${this.chatMember.chat.username}")
                }
            }
        }
    }
}