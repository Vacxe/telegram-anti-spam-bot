package io.github.vacxe.tgantispam.core.filters

import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.json.Json

@Serializable
data class CheckResponse(val ham: Float, val spam: Float)

class RemoteFilter(private val endpoint: String) : SpamFilter {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    override fun isSpam(input: String): Boolean {
        val request = Request.Builder()
            .url(endpoint)
            .build();

        client.newCall(request).execute().use { response ->
            response.body?.string()?.let { jsonString ->
                val checkResponse = json.decodeFromString<CheckResponse>(jsonString)
                return checkResponse.spam > checkResponse.ham
            }
        }
        // Fallback to false in case of problems with API
        return false
    }
}