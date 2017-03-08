package org.droba.mutantControllerDiscovery

import org.droba.mutant.Action
import org.droba.mutant.Mutant
import org.droba.mutant.MutantHalt
import org.droba.mutantControllerDiscovery.binders.ModelBinder
import org.droba.mutantControllerDiscovery.binders.PathAndQueryParamBinder
import kotlin.jvm.internal.Lambda
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

object MultiparamActionUtil {

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

        val invoker = getInvokerFor(actionLambda)

        val wrappedLambda = Mutant.act {

            val args = mutableListOf<Any>()

            req.pathParams.map { param -> findArgIndexAndValueByNamePartial(param) }
                    .plus(req.queryParams.map { param -> findArgIndexAndValueByNamePartial(param) })
                    .forEach { argIndexAndVal -> args.add(argIndexAndVal.first, argIndexAndVal.second) }

            if ( args.getOrNull(indexOfLastParam) == null)
                args.add(modelBinder.bindModel(typeOfLastParam, req))

            args.add(0, this)
            invoker(args)
        }

        return wrappedLambda
    }

    private fun findArgIndexAndValueByName(entry: Map.Entry<String, String>,
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

    @Suppress("UNCHECKED_CAST")
    private fun getInvokerFor(actionLambda: Lambda) : (List<Any>) -> Any {
        return when (actionLambda) {
            is Function2<*, *, *>                       -> { args: List<Any> -> (actionLambda as Function2<Any, Any, Any>).invoke(args[0], args[1]) }
            is Function3<*, *, *, *>                    -> { args: List<Any> -> (actionLambda as Function3<Any, Any, Any, Any>).invoke(args[0], args[1], args[2]) }
            is Function4<*, *, *, *, *>                 -> { args: List<Any> -> (actionLambda as Function4<Any, Any, Any, Any, Any>).invoke(args[0], args[1], args[2], args[3]) }
            is Function5<*, *, *, *, *, *>              -> { args: List<Any> -> (actionLambda as Function5<Any, Any, Any, Any, Any, Any>).invoke(args[0], args[1], args[2], args[3], args[4]) }
            is Function6<*, *, *, *, *, *, *>           -> { args: List<Any> -> (actionLambda as Function6<Any, Any, Any, Any, Any, Any, Any>).invoke(args[0], args[1], args[2], args[3], args[4], args[5]) }
            is Function7<*, *, *, *, *, *, *, *>        -> { args: List<Any> -> (actionLambda as Function7<Any, Any, Any, Any, Any, Any, Any, Any>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6]) }
            is Function8<*, *, *, *, *, *, *, *, *>     -> { args: List<Any> -> (actionLambda as Function8<Any, Any, Any, Any, Any, Any, Any, Any, Any>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]) }
            is Function9<*, *, *, *, *, *, *, *, *, *>  -> { args: List<Any> -> (actionLambda as Function9<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any>).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]) }
            else -> throw Exception("Error invoking lambda")
        }
    }
}