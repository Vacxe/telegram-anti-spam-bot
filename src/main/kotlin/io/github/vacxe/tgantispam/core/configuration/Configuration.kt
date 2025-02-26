package io.github.vacxe.tgantispam.core.configuration

import io.github.vacxe.tgantispam.core.filters.CombineFilter
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val token: String,
    val pollingTimeout: Int = 30,
    val chats: Map<Long, CombineFilter> = emptyMap(),
    val debug: Boolean = false,
    val goodBehaviourMessageCount: Int = Int.MAX_VALUE,
    val influxDb: InfluxDbConfiguration? = null,
)