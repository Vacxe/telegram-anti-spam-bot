package io.github.vacxe.tgantispam.core.linguistic

class RussianCharacterNormalizer: Transformer {
    override fun transform(input: String): String = input
        .replace("A", "А")
        .replace("B", "В")
        .replace("E", "Е")
        .replace("H", "Н")
        .replace("K", "К")
        .replace("P", "Р")
        .replace("O", "О")
        .replace("C", "С")
        .replace("M", "М")
        .replace("T", "Т")
        .replace("X", "Х")
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
        .replace("ё", "е")
        .replace("m", "м")
        .replace("k", "к")
        .replace("b", "в")
        .replace("ρ", "р")
        .replace("α", "а")
        .replace("κ", "к")
        .replace("3", "з")
}
