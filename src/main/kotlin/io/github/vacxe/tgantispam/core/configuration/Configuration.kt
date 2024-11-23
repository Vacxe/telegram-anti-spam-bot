package io.github.vacxe.tgantispam.core.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val token: String,
    val pollingTimeout: Int,
    val debug: Boolean,
    val influxDb: InfluxDbConfiguration? = null,
    val remoteFilterEndpoint: String? = null
)