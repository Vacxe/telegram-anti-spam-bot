package io.github.vacxe.tgantispam.core.filters

import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

@Serializable
data class CheckResponse(val ham: Float, val spam: Float)

class RemoteFilter(private val endpoint: String) : SpamFilter {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    override fun isSpam(input: String): Boolean {
        try {
            val request = Request.Builder()
                .url("$endpoint?text=${input.encode()}")  // Add URL parameter
                .get()
                .build();

            client.newCall(request).execute().use { response ->
                response.body?.string()?.let { jsonString ->
                    val checkResponse = json.decodeFromString<CheckResponse>(jsonString)
                    return checkResponse.spam > checkResponse.ham
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            System.err.println("Request to $endpoint failed, fallback to default value = false")
        }
        return false
    }
}