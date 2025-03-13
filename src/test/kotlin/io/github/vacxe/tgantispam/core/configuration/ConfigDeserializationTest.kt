package io.github.vacxe.tgantispam.core.configuration

import io.github.vacxe.tgantispam.Settings
import io.github.vacxe.tgantispam.core.filters.CombineFilter
import io.github.vacxe.tgantispam.core.filters.TestSpamFilter
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.measureTime

class ConfigDeserializationTest {

    @Test
    fun deserialization() {
        val configContent = javaClass.getResource("/filters.yaml")!!.readText()
        assertEquals(TestSpamFilter(), Settings.yaml.decodeFromString(configContent))
    }
}