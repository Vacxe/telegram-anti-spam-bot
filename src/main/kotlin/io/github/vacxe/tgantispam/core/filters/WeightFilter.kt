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
    quarantineWeight.toDouble(),
    banWeight.toDouble(),
    inputTransformer
) {
    override fun validateInput(input: String): SpamFilter.Result {

        var weight = 0
        val matches = hashSetOf<String>()

        restrictions.forEach {
            val findings = it.findAll(input).toList()
            if (findings.isNotEmpty()) {
                weight += findings.size
                matches += it.pattern
            }
        }

        val message = "Weights sum $weight: $matches"
        return report(weight.toDouble(), message)
    }
}
