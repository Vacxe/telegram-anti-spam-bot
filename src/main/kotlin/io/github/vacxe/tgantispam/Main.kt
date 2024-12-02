package io.github.vacxe.tgantispam

import com.charleskorn.kaml.Yaml
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.actions.commands.*
import io.github.vacxe.tgantispam.core.actions.events.ReceiveTextMessage
import io.github.vacxe.tgantispam.core.configuration.Configuration
import io.github.vacxe.tgantispam.core.data.Chat
import io.github.vacxe.tgantispam.core.filters.RemoteFilter
import io.github.vacxe.tgantispam.core.filters.RussianSpamFilter
import io.github.vacxe.tgantispam.core.filters.SpamFilter
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess

object Settings {
    var chats = HashSet<Chat>()
    lateinit var configuration: Configuration
}

private val json = Json {
    prettyPrint = true
}

fun main() {
    println("Init...")
    Settings.configuration = if (Files.configuration.exists()) {
        Yaml.default.decodeFromString(
            Configuration.serializer(),
            Files.configuration.readText()
        )
    } else {
        println("Configuration file not found")
        exitProcess(1)
    }
    println("Configuration loaded...")

    println("Loading chats configs...")
    Settings.chats = json.decodeFromString(Files.chats.readText())
    println("Chats configs loaded for ${Settings.chats.size} chats...")

    val logger = Logger(
        Settings.configuration.influxDb
    )

    val additionalFilters: List<SpamFilter> = Settings.configuration.remoteFilterEndpoint?.let {
        listOf(RemoteFilter(it))
    } ?: emptyList()

    val spamFilter = RussianSpamFilter(*additionalFilters.toTypedArray())

    val bot = bot {
        token = Settings.configuration.token
        timeout = Settings.configuration.pollingTimeout
        logLevel = LogLevel.Error

        dispatch {
            Systems::apply
            ReceiveTextMessage.apply(
                this,
                spamFilter,
                logger
            )

            ReportSpam.apply(
                this,
                logger
            )

            VerifyUser.apply(
                this,
                json,
            )
            BanUser::apply
            UnbanUser::apply
        }
    }

    bot.startPolling()
    println("Bot polling...")
}
