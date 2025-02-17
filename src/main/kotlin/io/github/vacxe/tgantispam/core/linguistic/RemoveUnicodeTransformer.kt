package io.github.vacxe.tgantispam.core.linguistic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("remove_unicode")
data object RemoveUnicodeTransformer : Transformer {
    private val unicode = Regex("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s[!-/][:-@][\\[-`][{-~]]")

    override fun transform(input: String): String = input.replace(unicode, "")
}
