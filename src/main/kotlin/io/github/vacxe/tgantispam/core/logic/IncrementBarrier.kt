package io.github.vacxe.tgantispam.core.logic

class IncrementBarrier<KEY>(private val barrier: Int) {
    private val map = hashMapOf<KEY, Int>()

    /**
     * @return return true if count of calls equals or more then barrier value
     */
    fun inc(key: KEY): Boolean {
        val oldValue = map.getOrDefault(key, 0)
        val newValue = oldValue + 1
        map[key] = newValue
        return newValue >= barrier
    }
}
