package io.github.vacxe.tgantispam.core.linguistic

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Polymorphic
@Serializable
sealed interface Transformer {
    fun transform(input: String): String
}