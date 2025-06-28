package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.filters.CombineFilter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

object Files {
    val configuration: File = File("data/config/config.yaml")

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

    fun chatFiltersFile(chatId: Long): File = File("data/chats/$chatId/filters.yaml")

    fun chatFilters(chatId: Long): CombineFilter =
        chatFiltersFile(chatId).let {
            if (it.exists()) {
                try {
                    Settings.yaml.decodeFromString(it.readText())
                } catch (e: Exception) {
                    println("Unable to load filters for chat ${chatId}: ${e.message}")
                    CombineFilter(emptyList())
                } finally {
                    println("Filters for chat $chatId loaded successfully")
                }
            } else {
                println("Unable to find filters for chat ${chatId}: empty filter applied")
                CombineFilter(emptyList())
            }
        }

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
