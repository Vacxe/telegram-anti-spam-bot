package io.github.vacxe.tgantispam.core

class RussianSpamFilter : Filter {
    private val filters = setOf<Filter>(StaticWordsFilter())

    override fun filter(message: String): Boolean = filters.all { it.filter(message) }
}
