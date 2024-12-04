package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer
import io.github.vacxe.tgantispam.core.linguistic.RemoveUnicodeTransformer

class RussianSpamFilter(vararg additionalFilters: SpamFilter) : CombineFilter(
    LanguageInjectionFilter(
        strictLanguage= Regex("[А-Яа-яЁё]"),
        name = "Injection in russian words",
        quarantineWeight = 2,
        banWeight = 3,
        inputTransformer = RemoveUnicodeTransformer()
    ),
    WeightFilter(
        name = "Strong restricted words",
        restrictions = setOf(
            "\\d+\\s*[$]",
            "[$]\\s*\\d+",
            "18\\s*\\+",
            "usd",
            "eur",
            "доход",
            "дохода",
            "доходность",
            "прибыль",
            "заработок",
            "заработка",
            "заработком",
            "оплата",
            "вознаграждением",
            "вознаграждение",
            "есть темка"
        ).map { Regex(it) }.toSet(),
        inputTransformer = CombineTransformer(
            RemoveUnicodeTransformer(),
            LowercaseTransformer(),
        )
    ),
    WeightFilter(
        name = "Common words",
        quarantineWeight = 3,
        banWeight = 5,
        restrictions = setOf(
            "[$]",
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
            "[+]",
            "удаленная",
            "удаленка",
            "долларов",
            "анкетирования",
            "\\d+",
            "смс",
            "темка"
        ).map { Regex(it) }.toSet(),
        inputTransformer = CombineTransformer(
            RemoveUnicodeTransformer(),
            LowercaseTransformer(),
        )
    ),
    *additionalFilters
)
