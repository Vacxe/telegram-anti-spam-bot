package io.github.vacxe.tgantispam.core.logic

import io.github.vacxe.tgantispam.core.Files
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GoodBehaviourManager(
    goodBehaviourMessageCount: Int = Int.MAX_VALUE,
) {
    private val goodBehaviorUsers = IncrementBarrier<Pair<Long, Long>>(goodBehaviourMessageCount)

    private val json = Json { prettyPrint = true }
    fun receiveMessageFrom(chatId: Long, userId: Long) {
        if (goodBehaviorUsers.inc(chatId to userId)) {
            println("Good Behaviour: User:$userId in Chat:$chatId")
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
