package io.github.vacxe.tgantispam.core

import java.util.*

class StaticWordsFilter : Filter {
    private val immediateBlock = setOf(
        "иsd", // "usd" transforms to "иsd", todo: split the filters
        "доход", "дохода", "заработок", "заработка", "заработком",
    )

    private var blockList = immediateBlock + setOf(
        "$",
        "в день",
        "в неделю",
        "в сутки",
        "за неделю",
        "дохода",
        "доходность",
        "комманду",
        "команда",
        "баксов",
        "удаленную",
        "удаленная",
        "требуются",
        "рублей",
        "занятость",
        "деятельность",
        "людей",
        "люди",
        "сотрудничество",
        "сотрудничества",
        "личные",
        "пишите",
        "дополнительный",
        "прибыль",
        "в лс",
        "набираем",
        "партнёров",
        "дохода",
        "срочно",
        "прибыль",
        "набираю",
        "+"
    )

    override fun filter(message: String): Boolean {
        val lowercaseMessage =
            message
                .lowercase(Locale.getDefault())
                .replace("a", "а")
                .replace("e", "е")
                .replace("o", "о")
                .replace("p", "р")
                .replace("c", "c")
                .replace("y", "у")
                .replace("x", "х")
                .replace("t", "т")
                .replace("u", "и")
                .replace("h", "н")

        immediateBlock.forEach {
            if (lowercaseMessage.contains(it)) {
                println("StaticWordsFilter: immediate block by $it")
                return false
            }
        }

        var weight = 0
        val matches = hashSetOf<String>()
        blockList.forEach {
            if (lowercaseMessage.contains(it)) {
                weight++
                matches += it
            }
        }

        println("StaticWordsFilter: weight $weight -> ${if (weight < 3) "Passed" else "Blocked"}")

        return weight < 3
    }
}