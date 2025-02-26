package io.github.vacxe.tgantispam.core.configuration

import io.github.vacxe.tgantispam.Settings
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConfigDeserializationTest {

    @Test
    fun deserialization() {
        val configContent = javaClass.getResource("/configuration.yaml")!!.readText()
        assertEquals(TestConfiguration(), Settings.yaml.decodeFromString(configContent))
    }
}