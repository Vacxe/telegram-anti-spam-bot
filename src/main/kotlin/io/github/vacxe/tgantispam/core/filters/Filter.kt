package io.github.vacxe.tgantispam.core.filters

interface Filter {
    fun filter(input: String): Boolean
}