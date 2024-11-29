package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

open class WeightFilter(
    private val restrictions: Set<Regex>,
    private val quarantineWeight: Int = 1,
    private val banWeight: Int = Int.MAX_VALUE,
    private val inputTransformer: Transformer = PassTransformer()
) : SpamFilter {
    override fun validate(input: String): SpamFilter.Decision {
        val transformedInput = inputTransformer.transform(input)

        var weight = 0
        val matches = hashSetOf<String>()

        restrictions.forEach {
            if (it.containsMatchIn(transformedInput)) {
                weight++
                matches += it.pattern
            }
        }

        val message = "WeightFilter: weights sum $weight not zero -> $matches"
        if (weight > 0) {
            println(message)
        }

        return when {
            weight >= banWeight -> SpamFilter.Decision.Ban(message)
            weight >= quarantineWeight -> SpamFilter.Decision.Quarantine(message)
            else -> SpamFilter.Decision.Pass
        }
    }
}
