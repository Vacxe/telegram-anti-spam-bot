package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer
import io.github.vacxe.tgantispam.core.linguistic.RussianCharacterNormalizer

class RussianSpamFilter : CombineFilter(
    BlockFilter(
        restrictedWords = setOf("usd"),
        inputTransformer = LowercaseTransformer()
    ),
    BlockFilter(
        restrictedWords = setOf(
            "доход",
            "дохода",
            "прибыль",
            "заработок",
            "заработка",
            "заработком",
            "прибыль"
        ),
        inputTransformer = CombineTransformer(
            LowercaseTransformer(),
            RussianCharacterNormalizer()
        )
    ),
    ConstantWeightFilter(
        maxWeight = 3,
        restrictedWords = setOf(
            "$",
            "в день",
            "в неделю",
            "неделя",
            "в сутки",
            "за неделю",
            "дохода",
            "доходность",
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
            "людей",
            "люди",
            "сотрудничество",
            "сотрудничества",
            "личные",
            "пишите",
            "дополнительный",
            "прибыль",
            "в лс",
            "рублей",
            "оплата",
            "набираем",
            "партнеров",
            "дохода",
            "срочно",
            "прибыль",
            "набираю",
            "+"
        ),
        inputTransformer = CombineTransformer(
            LowercaseTransformer(),
            RussianCharacterNormalizer()
        )
    )
)
