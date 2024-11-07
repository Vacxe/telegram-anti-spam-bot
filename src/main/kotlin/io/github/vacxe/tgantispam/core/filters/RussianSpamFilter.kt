package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer
import io.github.vacxe.tgantispam.core.linguistic.RussianCharacterNormalizer

class RussianSpamFilter : CombineFilter(
    BlockFilter(
        restrictedRegex = setOf(
            Regex("\\d+\\s*\\\$"),
            Regex("\\\$\\s*\\d+"),
            Regex("18\\s*\\+")
        )
    ),
    BlockFilter(
        restrictedWords = setOf("usd", "eur" /* E - latin */, "еur"/* E - cyrillic */),
        inputTransformer = LowercaseTransformer()
    ),
    BlockFilter(
        restrictedWords = setOf(
            "доход",
            "дохода",
            "доходность",
            "прибыль",
            "заработок",
            "заработка",
            "заработком",
            "прибыль",
            "оплата"
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
            "+",
            "удаленная",
            "удаленка",
            "долларов",
            "анкетирования"
        ),
        restrictedRegex = setOf(Regex("\\d+")),
        inputTransformer = CombineTransformer(
            LowercaseTransformer(),
            RussianCharacterNormalizer()
        )
    )
)
