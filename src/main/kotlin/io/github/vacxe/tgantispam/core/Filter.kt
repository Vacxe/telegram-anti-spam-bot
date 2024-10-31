package io.github.vacxe.tgantispam.core

interface Filter {
    fun filter(message: String): Boolean
}