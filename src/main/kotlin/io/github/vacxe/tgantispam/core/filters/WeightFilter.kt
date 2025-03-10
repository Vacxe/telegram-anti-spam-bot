package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.PassTransformer
import io.github.vacxe.tgantispam.core.linguistic.Transformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("weight")
data class WeightFilter(
    private val restrictionPatterns: Set<String>,
    override val name: String? = null,
    override val quarantineWeight: Double = 1.0,
    override val banWeight: Double = Double.MAX_VALUE,
    override val inputTransformer: Transformer = PassTransformer,
    override val enabled: Boolean = true,
) : BaseSpamFilter() {
    override fun validateInput(input: String): SpamFilter.Result {

        var weight = 0
        val matches = hashSetOf<String>()

        restrictionPatterns.map { Regex(it) }.forEach {
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
