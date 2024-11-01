package io.github.vacxe.tgantispam.core.filters

open class CombineFilter(vararg filtersArgs: Filter) : Filter {

    private val filters: List<Filter> = filtersArgs.toList()
    override fun filter(input: String): Boolean = filters.all { it.filter(input) }
}