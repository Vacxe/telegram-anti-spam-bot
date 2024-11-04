package io.github.vacxe.tgantispam.core.linguistic

class CombineTransformer(vararg transformerArgs: Transformer) : Transformer {

    private val transformers: List<Transformer> = transformerArgs.toList()
    override fun transform(input: String): String {
        var transformedInput = input
        transformers.forEach {
            transformedInput = it.transform(transformedInput)
        }
        return transformedInput
    }
}