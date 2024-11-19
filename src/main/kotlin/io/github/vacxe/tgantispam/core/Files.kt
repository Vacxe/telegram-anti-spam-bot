package io.github.vacxe.tgantispam.core

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.File

object Files {
    val configuration: File = File("data/config.yaml")
    val chats: File = File("data/chats.json")

    init {
        if (!chats.exists()) {
            chats.getParentFile().mkdirs()
            chats.createNewFile()
            chats.writeText(
                Json.encodeToString(
                    ListSerializer(String.serializer()),
                    emptyList()
                )
            )
        }
    }
}