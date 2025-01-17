package io.github.vacxe.tgantispam.core.logic

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class IncrementBarrierTest {

    @Test
    fun incTest() {
        val incrementBarrier = IncrementBarrier<Pair<Long, Long>>(2)
        assertFalse(incrementBarrier.inc(1L to 2L))
        assertTrue(incrementBarrier.inc(1L to 2L))
        assertTrue(incrementBarrier.inc(1L to 2L))
    }
}