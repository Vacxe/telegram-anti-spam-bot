package io.github.vacxe.tgantispam.core

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import com.influxdb.client.write.Point
import io.github.vacxe.tgantispam.core.configuration.InfluxDbConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant

class Logger(influxDbConfiguration: InfluxDbConfiguration? = null) {
    private val filteredSpamFile: File = File("data/logger/filtered_spam_messages.txt")
    private val unfilteredSpamFile: File = File("data/logger/unfiltered_spam_messages.txt")

    private val influxClient = influxDbConfiguration?.let {
        InfluxDBClientKotlinFactory.create(
            it.url,
            it.token.toCharArray(),
            it.org,
            it.bucket
        )
    }

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

    fun detectedSpamMessage(
        chatId: Long,
        message: String,
    ) {
        appendMessageToFile(filteredSpamFile, message)
        logEventToInflux(chatId, message, "SPAM")
    }

    fun reportedSpamMessage(
        chatId: Long,
        message: String
    ) {
        appendMessageToFile(unfilteredSpamFile, message)
        logEventToInflux(
            chatId, message,
            "SPAM_REPORT"
        )
    }

    fun receivedMessage(
        chatId: Long,
        message: String
    ) {
        logEventToInflux(
            chatId,
            message,
            "MESSAGE"
        )
    }

    private fun appendMessageToFile(
        file: File,
        message: String
    ) {
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

    private fun logEventToInflux(
        chatId: Long,
        message: String,
        messageType: String
    ) {
        influxClient?.getWriteKotlinApi()?.let { api ->
            val point = Point
                .measurement("message")
                .addTag("chatId", chatId.toString())
                .addTag("messageType", messageType)
                .addField("messageLength", message.length)
                .time(Instant.now(), WritePrecision.NS)

            GlobalScope.launch {
                api.writePoint(point)
            }
        }
    }
}
