package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer

class BlockFilter(
    private val restrictedWords: Set<String> = emptySet(),
    private val restrictedRegex: Set<Regex> = emptySet(),
    private val inputTransformer: Transformer = PassTransformer()
) : Filter {
    override fun filter(input: String): Boolean {
        val transformedInput = inputTransformer.transform(input)
        val passed = restrictedWords.none { transformedInput.contains(it) } &&
                restrictedRegex.none { it.containsMatchIn(transformedInput) }

        if (!passed) {
            println("Blocked by BlockFilter: contained restricted words $restrictedWords or regex $restrictedRegex")
        }
        return passed
    }
}
