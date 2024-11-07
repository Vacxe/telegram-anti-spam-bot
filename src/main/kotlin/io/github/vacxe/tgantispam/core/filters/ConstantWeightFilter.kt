package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

class ConstantWeightFilter(
    private val maxWeight: Int,
    private val restrictedWords: Set<String>,
    private val restrictedRegex: Set<Regex> = emptySet(),
    private val inputTransformer: Transformer = PassTransformer()
) : Filter {
    override fun filter(input: String): Boolean {
        val transformedInput = inputTransformer.transform(input)

        var weight = 0
        val matches = hashSetOf<String>()

        restrictedWords.forEach {
            if (transformedInput.contains(it)) {
                weight++
                matches += it
            }
        }

        restrictedRegex.forEach {
            if (it.containsMatchIn(transformedInput)) {
                weight++
            }
        }

        val passed = weight <= maxWeight

        if (weight != 0) {
            println("ConstantWeightFilter: weights sum $weight > $maxWeight -> $matches")
        }

        return passed
    }
}
