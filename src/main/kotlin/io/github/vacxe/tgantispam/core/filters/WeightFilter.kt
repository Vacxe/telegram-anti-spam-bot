package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

open class WeightFilter(
    private val restrictions: Set<Regex>,
    private val maxWeight: Int = 0,
    private val inputTransformer: Transformer = PassTransformer()
) : SpamFilter {
    override fun isSpam(input: String): Boolean {
        val transformedInput = inputTransformer.transform(input)

        var weight = 0
        val matches = hashSetOf<String>()

        restrictions.forEach {
            if (it.containsMatchIn(transformedInput)) {
                weight++
                matches += it.pattern
            }
        }

        val spam = weight > maxWeight

        if (weight != 0) {
            println("WeightFilter: weights sum $weight > $maxWeight -> $matches")
        }

        return spam
    }
}
