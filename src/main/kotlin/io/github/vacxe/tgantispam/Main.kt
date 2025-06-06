package io.github.vacxe.tgantispam

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.vacxe.tgantispam.core.Files
import io.github.vacxe.tgantispam.core.Logger
import io.github.vacxe.tgantispam.core.actions.commands.BanUser.banUser
import io.github.vacxe.tgantispam.core.actions.commands.Captcha.captcha
import io.github.vacxe.tgantispam.core.actions.commands.ReportSpam.reportSpam
import io.github.vacxe.tgantispam.core.actions.commands.Systems.systems
import io.github.vacxe.tgantispam.core.actions.commands.UnbanUser.unbanUser
import io.github.vacxe.tgantispam.core.actions.commands.UploadChatFilters.uploadChatFilters
import io.github.vacxe.tgantispam.core.actions.commands.VerifyUser.verifyUser
import io.github.vacxe.tgantispam.core.actions.events.ReceiveTextMessage.receiveTextMessage
import io.github.vacxe.tgantispam.core.configuration.Configuration
import io.github.vacxe.tgantispam.core.data.Chat
import io.github.vacxe.tgantispam.core.filters.*
import io.github.vacxe.tgantispam.core.linguistic.*
import io.github.vacxe.tgantispam.core.logic.GoodBehaviourManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.system.exitProcess

object Settings {
    var chats = HashSet<Chat>()
    lateinit var configuration: Configuration
    lateinit var goodBehaviourManager: GoodBehaviourManager
    lateinit var logger: Logger
    val spamFilters: HashMap<Long, CombineFilter> = HashMap()

    val json = Json {
        prettyPrint = true
    }

    private val serializersModule = SerializersModule {
        polymorphic(SpamFilter::class) {
            subclass(RemoteFilter::class, RemoteFilter.serializer())
            subclass(WeightFilter::class, WeightFilter.serializer())
            subclass(LanguageInjectionFilter::class, LanguageInjectionFilter.serializer())
        }
        polymorphic(Transformer::class) {
            subclass(PassTransformer::class, PassTransformer.serializer())
            subclass(RemoveUnicodeTransformer::class, RemoveUnicodeTransformer.serializer())
            subclass(LowercaseTransformer::class, LowercaseTransformer.serializer())
            subclass(CombineTransformer::class, CombineTransformer.serializer())
        }
    }

    val yaml = Yaml(
        configuration = YamlConfiguration(
            polymorphismStyle = PolymorphismStyle.Property,
        ),
        serializersModule = serializersModule
    )
}

fun main() {
    println("Init...")
    Settings.configuration = if (Files.configuration.exists()) {
        Settings.yaml.decodeFromString(
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

    for (chat in Settings.chats) {
        Settings.spamFilters[chat.id] = Files.chatFilters(chat.id)
    }

    Settings.logger = Logger(Settings.configuration.influxDb)

    println("Good behaviour message count: ${Settings.configuration.goodBehaviourMessageCount}")
    Settings.goodBehaviourManager = GoodBehaviourManager(Settings.configuration.goodBehaviourMessageCount)

    val bot = bot {
        token = Settings.configuration.token
        timeout = Settings.configuration.pollingTimeout
        logLevel = LogLevel.Error

        dispatch {
            apply {
                systems()
                receiveTextMessage(Settings.logger)
                reportSpam(Settings.logger)
                verifyUser(Settings.json)
                banUser()
                unbanUser()
                captcha()
                uploadChatFilters()
            }
        }
    }

    bot.startPolling()
    println("Bot polling...")
}
