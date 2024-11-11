package io.github.vacxe.tgantispam.core

import java.io.File

class Logger(
    private val filteredSpamFile: File = File("data/logger/filtered_spam_messages.txt"),
    private val unfilteredSpamFile: File = File("data/logger/unfiltered_spam_messages.txt")
) {
    init {
        if (!filteredSpamFile.exists()) {
            filteredSpamFile.getParentFile().mkdirs()
            filteredSpamFile.createNewFile()
        }

        if (!unfilteredSpamFile.exists()) {
            unfilteredSpamFile.getParentFile().mkdirs()
            unfilteredSpamFile.createNewFile()
        }
    }

    fun receivedSpamMessage(
        message: String,
        detected: Boolean
    ) {
        val file = if(detected) filteredSpamFile else unfilteredSpamFile
        file.appendText(
            message
                .replace("\n", " ")
                .replace(
                    Regex("\\s{2,}"),
                    " "
                )
        )
        file.appendText("\n")
    }
}
