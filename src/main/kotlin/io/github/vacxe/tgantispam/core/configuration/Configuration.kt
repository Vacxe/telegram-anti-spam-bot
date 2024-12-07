package io.github.vacxe.tgantispam.core.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val token: String,
    val pollingTimeout: Int = 30,
    val debug: Boolean = false,
    val goodBehaviourMessageCount: Int = Int.MAX_VALUE,
    val influxDb: InfluxDbConfiguration? = null,
    val remoteFilterEndpoint: String? = null
)