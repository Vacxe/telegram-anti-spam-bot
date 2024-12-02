package io.github.vacxe.tgantispam.core.logic

import com.github.kotlintelegrambot.entities.Message
import io.github.vacxe.tgantispam.core.actions.events.ReceiveTextMessage

class GoodBehaviourManager(
    private val goodBehaviourMessageCount: Int = Int.MAX_VALUE,
) {
    private val goodBehaviorUsers = hashMapOf<Long, Int>()
    fun receiveMessage(message: Message) {
        message.from?.id?.let { userId ->
            goodBehaviorUsers.put(
                userId,
                goodBehaviorUsers.getOrDefault(userId, 0) + 1
            )?.let {
                if (it >= goodBehaviourMessageCount) {
                    // TODO: Implement logic
                }
            }
        }

    }

}