package io.github.vacxe.tgantispam.core

import io.github.vacxe.tgantispam.core.filters.RemoteFilter
import io.github.vacxe.tgantispam.core.filters.SpamFilter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
class RemoteFilterTest {

    @Container
    val classifier = GenericContainer(DockerImageName.parse("bert-spam-classifier"))
        .withExposedPorts(8000)
        .withEnv("HG_TOKEN",System.getenv("HG_TOKEN"))

    private fun createFilter(): RemoteFilter {
        val endpoint = "http://${classifier.host}:${classifier.firstMappedPort}/check"
        return RemoteFilter(endpoint)
    }

    @Test
    fun testSpam() {
        val filter = createFilter()
        assertTrue(filter.validate("Привет, нужны люди ,( С телефона, или  компьютера)   От 220\$ в день. Удалённая занятость , Подробности в ЛС") is SpamFilter.Result.Quarantine)
    }

    @Test
    fun testHam() {
        val filter = createFilter()
        assertTrue(filter.validate("Я хочу, чтобы при создании Pull Request запускались только релевантные UI-тесты, чтобы избежать избыточного выполнения всех тестов. То есть тесты которые были затронуты кодом в Git Diff.\n") is SpamFilter.Result.Pass)
    }
}
