package io.github.vacxe.tgantispam.core.filters

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

// TODO: make it polymorphic SpamFilter
@Serializable
data class CombineFilter(private val filters: List<@Polymorphic SpamFilter>) {

    fun validate(input: String): List<SpamFilter.Result> {
        val checks = arrayListOf<SpamFilter.Result>()

        filters.forEach {
            val result = it.validate(input)
            checks += result
            if (result.weight == Int.MAX_VALUE) {
                return checks
            }
        }

        return checks
    }
}
