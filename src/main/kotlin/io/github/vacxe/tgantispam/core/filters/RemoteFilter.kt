package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

@Serializable
data class CheckResponse(val ham: Float, val spam: Float)

class RemoteFilter(
    name: String,
    private val endpoint: String,
    quarantineWeight: Double = 0.5,
    banWeight: Double = Double.MAX_VALUE,
) : BaseSpamFilter(
    name,
    quarantineWeight,
    banWeight,
    PassTransformer()
) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    override fun validateInput(input: String): SpamFilter.Result {
        try {
            val request = Request.Builder()
                .url("$endpoint?text=${input.encode()}")  // Add URL parameter
                .get()
                .build();

            client.newCall(request).execute().use { response ->
                response.body?.string()?.let { jsonString ->
                    val checkResponse = json.decodeFromString<CheckResponse>(jsonString)
                    return report(checkResponse.spam.toDouble(), "Classification as Spam: ${checkResponse.spam.toDouble()}")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            System.err.println("Request to $endpoint failed, fallback to default value = IGNORE_MESSAGE")
        }
        return SpamFilter.Result.Pass()
    }
}