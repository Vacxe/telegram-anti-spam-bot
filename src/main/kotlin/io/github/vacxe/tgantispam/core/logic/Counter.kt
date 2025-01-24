package io.github.vacxe.tgantispam.core.logic

class Counter<KEY> {
    private val map = hashMapOf<KEY, Int>()

    /**
     * @return return true if count of calls equals or more then barrier value
     */
    fun inc(key: KEY): Int {
        val value = map.getOrDefault(key, 0) + 1
        map[key] = value
        return value
    }
}
