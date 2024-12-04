package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

open class WeightFilter(
    private val restrictions: Set<Regex>,
    name: String? = null,
    quarantineWeight: Int = 1,
    banWeight: Int = Int.MAX_VALUE,
    inputTransformer: Transformer = PassTransformer()
) : BaseSpamFilter(
    name,
    quarantineWeight,
    banWeight,
    inputTransformer
) {
    override fun validateInput(input: String): SpamFilter.Result {

        var weight = 0
        val matches = hashSetOf<String>()

        restrictions.forEach {
            if (it.containsMatchIn(input)) {
                weight++
                matches += it.pattern
            }
        }

        val message = "Weights sum $weight: $matches"
        return report(weight, message)
    }
}
