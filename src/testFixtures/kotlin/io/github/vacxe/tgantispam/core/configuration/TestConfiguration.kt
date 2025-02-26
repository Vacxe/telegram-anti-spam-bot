@file:Suppress("TestFunctionName")

package io.github.vacxe.tgantispam.core.configuration

import io.github.vacxe.tgantispam.core.filters.TestSpamFilter

fun TestConfiguration() = Configuration(
    token = "<BOT-TOKEN>",
    pollingTimeout = 10,
    chats = mapOf(
        -1001181570704L to TestSpamFilter(),
    ),
)