@file:Suppress("TestFunctionName")

package io.github.vacxe.tgantispam.core.filters

import io.github.vacxe.tgantispam.core.linguistic.RemoveUnicodeTransformer
import io.github.vacxe.tgantispam.core.linguistic.CombineTransformer
import io.github.vacxe.tgantispam.core.linguistic.LowercaseTransformer

fun TestSpamFilter() = CombineFilter(
    filters = listOf(
        LanguageInjectionFilter(
            strictLanguagePattern = "[А-Яа-яЁё]",
            name = "Injection in russian words",
            quarantineWeight = 2.0,
            banWeight = 3.0,
            inputTransformer = RemoveUnicodeTransformer,
        ),
        WeightFilter(
            name = "Emoji Limit",
            restrictionPatterns = setOf(
                "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s[!-/][:-@][\\[-`][{-~]]"
            ),
            quarantineWeight = 5.0,
            banWeight = 10.0
        ),
        RemoteFilter(
            name = "AI Spam Model",
            endpoint = "http://192.168.1.100:8100/check",
            quarantineWeight = 0.3,
            banWeight = 0.98,
            minMessageLengthForCheck = 40,
            enabled = false
        ),
        WeightFilter(
            name = "Strong restricted words",
            restrictionPatterns = setOf(
                "\\d+\\s*[$]",
                "[$]\\s*\\d+",
                "18\\s*[+]",
                "\\s\\d{3}к",
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
                "есть темка",
                "выиграл",
            ).toSet(),
            quarantineWeight = 1.0,
            banWeight = 2.0,
            inputTransformer = CombineTransformer(
                listOf(
                    RemoveUnicodeTransformer,
                    LowercaseTransformer,
                )
            ),
        ),
        WeightFilter(
            name = "Common words",
            quarantineWeight = 3.0,
            banWeight = 5.0,
            restrictionPatterns = setOf(
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
                "\\d{3,}",
                "смс",
                "темка",
            ).toSet(),
            inputTransformer = CombineTransformer(
                listOf(
                    RemoveUnicodeTransformer,
                    LowercaseTransformer,
                )
            ),
        ),
    )
)
