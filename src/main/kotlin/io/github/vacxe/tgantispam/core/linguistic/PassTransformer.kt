package io.github.vacxe.tgantispam.core.linguistic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("pass")
data object PassTransformer : Transformer {
    override fun transform(input: String): String = input
}