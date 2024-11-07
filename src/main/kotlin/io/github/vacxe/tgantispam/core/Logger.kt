package io.github.vacxe.tgantispam.core

import java.io.File

class Logger(
    private val filteredSpamFile: File = File("data/logger/filtered_spam_messages.txt")
) {
    init {
        if (!filteredSpamFile.exists()) {
            filteredSpamFile.createNewFile()
        }
    }

    fun receivedSpamMessage(message: String) {
        filteredSpamFile.appendText(
            message
                .replace("\n", " ")
                .replace(
                    Regex("\\s{2,}"),
                    " "
                )
        )
        filteredSpamFile.appendText("\n")
    }
}
