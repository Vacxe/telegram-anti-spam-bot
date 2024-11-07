package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer
import io.github.vacxe.tgantispam.core.linguistic.RussianCharacterTransformer

class RussianSpamFilter : CombineFilter(
    WeightFilter(
        restrictions = setOf(
            Regex("\\d+\\s*\\\$"),
            Regex("\\\$\\s*\\d+"),
            Regex("18\\s*\\+"),
            Regex("usd"),
            Regex("eur"), /* E - latin */
            Regex("еur") /* E - cyrillic */
        ),
        inputTransformer = LowercaseTransformer()
    ),
    WeightFilter(
        restrictions = setOf(
            "доход",
            "дохода",
            "доходность",
            "прибыль",
            "заработок",
            "заработка",
            "заработком",
            "оплата"
        ).map { Regex(it) }.toSet(),
        inputTransformer = CombineTransformer(
            LowercaseTransformer(),
            RussianCharacterTransformer()
        )
    ),
    WeightFilter(
        maxWeight = 3,
        restrictions = setOf(
            "\\\$",
            "день",
            "долларов",
            "приглашаю",
            "еженедельно",
            "пиши плюс",
            "получай",
            "в неделю",
            "неделя",
            "в сутки",
            "за неделю",
            "дохода",
            "доходность",
            "зарабатывать",
            "комманду",
            "команда",
            "баксов",
            "удаленную",
            "удаленная",
            "удаленной",
            "требуются",
            "рублей",
            "занятость",
            "деятельность",
            "занятость",
            "людей",
            "люди",
            "сотрудничество",
            "сотрудничества",
            "личные",
            "пишите",
            "дополнительный",
            "прибыль",
            "оплата",
            "без опыта",
            "удаленная",
            "работу",
            "в команду",
            "зарабатывать",
            "в лс",
            "рублей",
            "оплата",
            "набираем",
            "партнеров",
            "ответственных",
            "дохода",
            "срочно",
            "прибыль",
            "набираю",
            "\\+",
            "удаленная",
            "удаленка",
            "долларов",
            "анкетирования",
            "\\d+",
            "смс"
        ).map { Regex(it) }.toSet(),
        inputTransformer = CombineTransformer(
            LowercaseTransformer(),
            RussianCharacterTransformer()
        )
    )
)
