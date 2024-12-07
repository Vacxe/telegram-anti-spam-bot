package io.github.vacxe.tgantispam

import com.charleskorn.kaml.Yaml
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.actions.commands.BanUser.banUser
import io.github.vacxe.tgantispam.core.actions.commands.ReportSpam.reportSpam
import io.github.vacxe.tgantispam.core.actions.commands.Systems.systems
import io.github.vacxe.tgantispam.core.actions.commands.UnbanUser.unbanUser
import io.github.vacxe.tgantispam.core.actions.commands.VerifyUser.verifyUser
import io.github.vacxe.tgantispam.core.actions.events.ReceiveTextMessage.receiveTextMessage
import io.github.vacxe.tgantispam.core.configuration.Configuration
import io.github.vacxe.tgantispam.core.data.Chat
import io.github.vacxe.tgantispam.core.filters.CombineFilter
import io.github.vacxe.tgantispam.core.filters.KakaoSpamFilter
import io.github.vacxe.tgantispam.core.logic.GoodBehaviourManager
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess

object Settings {
    var chats = HashSet<Chat>()
    lateinit var configuration: Configuration
    lateinit var goodBehaviourManager: GoodBehaviourManager
    val chatFiltersConfigurations: HashMap<Long, CombineFilter> = hashMapOf()
    lateinit var logger: Logger
    val json = Json {
        prettyPrint = true
    }
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
    Settings.chats = Settings.json.decodeFromString(Files.chats.readText())
    println("Chats configs loaded for ${Settings.chats.size} chats...")

    Settings.logger = Logger(Settings.configuration.influxDb)
    Settings.goodBehaviourManager = GoodBehaviourManager(Settings.configuration.goodBehaviourMessageCount)
    Settings.chatFiltersConfigurations[-1001181570704L] = KakaoSpamFilter()

    val bot = bot {
        token = Settings.configuration.token
        timeout = Settings.configuration.pollingTimeout
        logLevel = LogLevel.Error

        dispatch {
            apply {
                systems()
                receiveTextMessage(Settings.chatFiltersConfigurations, Settings.logger)
                reportSpam(Settings.logger)
                verifyUser(Settings.json)
                banUser()
                unbanUser()
            }
        }
    }

    bot.startPolling()
    println("Bot polling...")
}
