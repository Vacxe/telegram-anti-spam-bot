package io.github.vacxe.tgantispam.core.logic

import kotlin.test.Test

class CounterTest {

    @Test
    fun incTest() {
        val incrementBarrier = Counter<Pair<Long, Long>>()
        kotlin.test.assertEquals(1, incrementBarrier.inc(1L to 2L))
        kotlin.test.assertEquals(2, incrementBarrier.inc(1L to 2L))
        kotlin.test.assertEquals(3, incrementBarrier.inc(1L to 2L))

        kotlin.test.assertEquals(1, incrementBarrier.inc(2L to 1L))
    }
}
