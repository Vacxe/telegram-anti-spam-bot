package io.github.vacxe.tgantispam.core.filters

open class CombineFilter(vararg filtersArgs: SpamFilter) {

    private val spamFilters: List<SpamFilter> = filtersArgs.toList()
    fun validate(input: String): List<SpamFilter.Decision> {
        var decision: SpamFilter.Decision = SpamFilter.Decision.Pass
        var checks = arrayListOf<SpamFilter.Decision>()

        spamFilters.forEach {
            val result = it.validate(input)
            checks += result
            if (result.weight > decision.weight) {
                decision = result
                if (decision.weight == Int.MAX_VALUE) {
                    return checks
                }
            }
        }

        return checks
    }
}
