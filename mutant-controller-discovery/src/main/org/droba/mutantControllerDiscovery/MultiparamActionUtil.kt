package org.droba.mutantControllerDiscovery

import org.droba.mutant.Action
import org.droba.mutant.M
import org.droba.mutant.Mutant
import org.droba.mutant.MutantHalt
import org.droba.mutantControllerDiscovery.binders.ModelBinder
import org.droba.mutantControllerDiscovery.binders.PathAndQueryParamBinder
import kotlin.jvm.internal.Lambda
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

object MultiparamActionUtil {

    fun findArgIndexAndValueByName(entry: Map.Entry<String, String>,
                                   lambdaParams: List<KParameter>,
                                   pathAndQueryParamBinder: PathAndQueryParamBinder) : Pair<Int, Any> {

        var lambdaParam: KParameter?    = null
        var indexOfLambaParam: Int?     = null

        val paramName  = entry.key.removePrefix(":")
        val paramValue = entry.value

        for((index, param) in lambdaParams.withIndex()) {
           if (param.name == paramName) {
               lambdaParam       = param
               indexOfLambaParam = index
               break
           }
        }

        if (indexOfLambaParam == null || lambdaParam == null)
            throw MutantHalt(400, "No appropriate path found - param '$paramName' could not be bound!")

        val argValue = pathAndQueryParamBinder.bindPathParam(paramValue, lambdaParam.type)

        return indexOfLambaParam to argValue
    }

    /**
     *  Wraps multi parameter actionLambda into a single _binding_ M receiver lambda.
     *
     *  The new lambda will be binding in the sense that it will try to provide the necessary
     *  arguments for the multi parameter actionLambda by using the provided `ModelBinder` and
     *  `PathAndQueryParamBinder`
     */
    fun wrapWithBindingAction(actionLambda: Lambda,
                              lambdaIntrospect: KFunction<*>,
                              modelBinder: ModelBinder,
                              pathAndQueryParamBinder: PathAndQueryParamBinder) : Action {

        val lambdaParamsWithoutReceiver = lambdaIntrospect.parameters
                .slice(1..lambdaIntrospect.parameters.size - 1)

        val indexOfLastParam = lambdaParamsWithoutReceiver.lastIndex
        val typeOfLastParam  = lambdaParamsWithoutReceiver.last().type

        val findArgIndexAndValueByNamePartial = { param: Map.Entry<String, String> ->
            findArgIndexAndValueByName(param, lambdaParamsWithoutReceiver, pathAndQueryParamBinder)
        }

        val wrappedLambda = Mutant.act {

            val args = mutableListOf<Any>()

            req.pathParams.map { param -> findArgIndexAndValueByNamePartial(param) }
                    .plus(req.params.map { param -> findArgIndexAndValueByNamePartial(param) })
                    .forEach { argIndexAndVal -> args.add(argIndexAndVal.first, argIndexAndVal.second) }

            if ( args.getOrNull(indexOfLastParam) == null)
                args.add(modelBinder.bindModel(typeOfLastParam, req))

            val result = when (actionLambda.arity) {
                2 -> actionLambda.invoke(this, args[0])
                3 -> actionLambda.invoke(this, args[0], args[1])
                4 -> actionLambda.invoke(this, args[0], args[1], args[2])
                5 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3])
                6 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4])
                7 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5])
                8 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6])
                9 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
                10 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
                11 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])
                12 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10])
                13 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11])
                14 -> actionLambda.invoke(this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12])
                else -> throw Exception("Error constructing lambda")
            }

            result ?: throw Exception("Error constructing lambda")
        }

        return wrappedLambda
    }
}