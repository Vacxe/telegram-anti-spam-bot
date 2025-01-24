package io.github.vacxe.tgantispam.core.logic

import io.github.vacxe.tgantispam.core.Files
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GoodBehaviourManager(
    private val goodBehaviourMessageCount: Int = Int.MAX_VALUE,
) {
    private val counter = Counter<Pair<Long, Long>>()

    private val json = Json { prettyPrint = true }
    fun receiveMessageFrom(chatId: Long, userId: Long) {
        if (counter.inc(chatId to userId) >= goodBehaviourMessageCount) {
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
