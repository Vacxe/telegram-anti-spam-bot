package io.github.vacxe.tgantispam.core

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.File

object Files {
    val configuration: File = File("data/config.yaml")

    fun filteredSpamFile(chatId: Long): File =
        getOrCreate(File("data/chats/$chatId/filtered_spam_messages.txt"))

    fun unfilteredSpamFile(chatId: Long): File =
        getOrCreate(File("data/chats/$chatId/unfiltered_spam_messages.txt"))

    val chats: File = getOrCreate(
        File("data/chats/chats.json"),
        Json.encodeToString(
            ListSerializer(String.serializer()),
            emptyList()
        )
    )

    fun verifiedUsers(chatId: Long) = getOrCreate(
        File("data/chats/$chatId/verified_users.json"),
        Json.encodeToString(
            ListSerializer(Int.serializer()),
            emptyList()
        )
    )

    private fun getOrCreate(file: File, defaultContent: String? = null): File {
        if (!file.exists()) {
            file.getParentFile().mkdirs()
            file.createNewFile()
            defaultContent?.let {
                file.writeText(it)
            }
        }
        return file
    }
}
