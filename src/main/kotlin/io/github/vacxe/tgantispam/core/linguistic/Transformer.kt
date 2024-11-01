package io.github.vacxe.tgantispam.core.linguistic

interface Transformer {
    fun transform(input: String): String
}