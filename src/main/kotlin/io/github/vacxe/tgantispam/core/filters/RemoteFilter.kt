package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

@Serializable
data class CheckResponse(val ham: Float, val spam: Float)

@Serializable
@SerialName("remote_filter")
data class RemoteFilter(
    private val endpoint: String,
    private val minMessageLengthForCheck: Int = 0,
    override val name: String?,
    override val quarantineWeight: Double = 0.5,
    override val banWeight: Double = Double.MAX_VALUE,
    override val inputTransformer: Transformer = PassTransformer,
    override val enabled: Boolean = true,
) : BaseSpamFilter() {
    @Transient private val client = OkHttpClient()
    @Transient private val json = Json { ignoreUnknownKeys = true }

    override fun validateInput(input: String): SpamFilter.Result {
        if (input.length > minMessageLengthForCheck)
            try {
                val request = Request.Builder()
                    .url("$endpoint?text=${input.encode()}")  // Add URL parameter
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    response.body?.string()?.let { jsonString ->
                        val checkResponse = json.decodeFromString<CheckResponse>(jsonString)
                        return report(
                            checkResponse.spam.toDouble(),
                            "Classification as Spam: ${checkResponse.spam.toDouble()}"
                        )
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                System.err.println("Request to $endpoint failed, fallback to default value = IGNORE_MESSAGE")
                return SpamFilter.Result.Pass("Request to $endpoint failed")
            }

        return SpamFilter.Result.Pass()
    }
}