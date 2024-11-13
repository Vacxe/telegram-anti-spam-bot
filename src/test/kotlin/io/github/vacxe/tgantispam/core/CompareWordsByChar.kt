package io.github.vacxe.tgantispam.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CompareWordsByChar {

    //@Test
    fun compare() {
        val bad = "еur"
        val good = "eur"

        for(i in bad.indices) {
            val a = bad[i].code
            val b = good[i].code
            assertEquals(bad[i], good[i])
        }
    }
}
