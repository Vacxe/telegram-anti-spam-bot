package io.github.vacxe.tgantispam.core.linguistic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("lowercase")
data object LowercaseTransformer : Transformer {
    override fun transform(input: String) = input.lowercase(Locale.getDefault())
}
