package io.github.vacxe.tgantispam.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CompareWordsByChar {

    //@Test
    fun compare() {
        val word1 = "заработок"
        val word2 = "заработоκ"

        for(i in word1.indices) {
            assertEquals(word1[i], word2[i])
        }
    }
}
