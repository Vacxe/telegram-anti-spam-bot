package io.github.vacxe.tgantispam.core.filters

interface SpamFilter {
    fun isSpam(input: String): Boolean
}