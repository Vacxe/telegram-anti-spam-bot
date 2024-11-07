package io.github.vacxe.tgantispam.core.filters

open class CombineFilter(vararg filtersArgs: SpamFilter) : SpamFilter {

    private val spamFilters: List<SpamFilter> = filtersArgs.toList()
    override fun isSpam(input: String): Boolean = spamFilters.any { it.isSpam(input) }
}