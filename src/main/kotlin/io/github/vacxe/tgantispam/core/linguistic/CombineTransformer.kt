package io.github.vacxe.tgantispam.core.linguistic

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("combine")
data class CombineTransformer(private val transformers: List<@Polymorphic Transformer>) : Transformer {

    override fun transform(input: String): String {
        var transformedInput = input
        transformers.forEach {
            transformedInput = it.transform(transformedInput)
        }
        return transformedInput
    }
}