package io.github.vacxe.tgantispam.core.configuration

import kotlinx.serialization.Serializable

@Serializable
data class InfluxDbConfiguration(
    val url: String,
    val token: String,
    val org: String,
    val bucket: String,
)
