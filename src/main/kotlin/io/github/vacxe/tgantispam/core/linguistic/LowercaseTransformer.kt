package io.github.vacxe.tgantispam.core.linguistic

import java.util.*

class LowercaseTransformer : Transformer {
    override fun transform(input: String) = input.lowercase(Locale.getDefault())

}
