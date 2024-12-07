package io.github.vacxe.tgantispam.core.logic

import com.github.kotlintelegrambot.entities.Message
import io.github.vacxe.tgantispam.core.Files
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GoodBehaviourManager(
    private val goodBehaviourMessageCount: Int = Int.MAX_VALUE,
) {
    private val goodBehaviorUsers = hashMapOf<Pair<Long, Long>, Int>()

    private val json = Json { prettyPrint = true }
    fun receiveMessage(message: Message) {
        val chatId = message.chat.id
        val userId = message.from?.id

        if (userId != null) {
            goodBehaviorUsers.put(
                chatId to userId,
                goodBehaviorUsers.getOrDefault(userId to userId, 0) + 1
            )?.let { messagesFromUser ->
                if (messagesFromUser >= goodBehaviourMessageCount) {
                    val verifiedUsersFile = Files.verifiedUsers(chatId)
                    val verifiedUsers: HashSet<Long> = Json
                        .decodeFromString<Set<Long>>(verifiedUsersFile.readText())
                        .toHashSet()
                    verifiedUsers.add(userId)
                    verifiedUsersFile.writeText(
                        json.encodeToString(verifiedUsers)
                    )
                }
            }
        }
    }
}