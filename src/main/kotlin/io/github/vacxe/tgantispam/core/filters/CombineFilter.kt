package io.github.vacxe.tgantispam.core.filters

open class CombineFilter(vararg filtersArgs: SpamFilter) {

    private val spamFilters: List<SpamFilter> = filtersArgs.toList()
    fun validate(input: String): List<SpamFilter.Result> {
        val checks = arrayListOf<SpamFilter.Result>()

        spamFilters.forEach {
            val result = it.validate(input)
            checks += result
            if (result.weight == Int.MAX_VALUE) {
                return checks
            }
        }

        return checks
    }
}
