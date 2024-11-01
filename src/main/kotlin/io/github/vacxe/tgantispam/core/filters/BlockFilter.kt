package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

class BlockFilter(
    private val restrictedWords: Set<String>,
    private val inputTransformer: Transformer = PassTransformer()
) : Filter {
    override fun filter(input: String): Boolean {
        val transformedInput = inputTransformer.transform(input)
        val passed = restrictedWords.none { transformedInput.contains(it) }

        if (!passed) {
            println("Blocked by BlockFilter: contained restricted words $restrictedWords")
        }
        return passed
    }
}
