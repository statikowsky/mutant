package org.droba.mutantControllerDiscovery.binders.mutantBinders

import mu.KotlinLogging
import org.droba.mutant.MutantRequest
import org.droba.mutantControllerDiscovery.binders.ModelBinder
import org.droba.mutantGsonJsonRenderer.GsonJsonRenderer
import kotlin.reflect.KType
import kotlin.reflect.defaultType
import kotlin.reflect.primaryConstructor

fun modelBinder (modelConfig: MutantModelBinder.() -> Unit) : MutantModelBinder {
    val modelBinder = MutantModelBinder()
    modelBinder.modelConfig()
    return modelBinder
}

class MutantModelBinder : ModelBinder {

    val log = KotlinLogging.logger {  }

    // Kotlin reflection does not yet support full reflection
    // over lambdas - specifically getting the parameters KType's
    // java class. (For KType type - running type.javaType throws Internal
    // reflection exception explaining that it's not yet implemented
    // for lambdas)
    //
    // As we need this information for Gson we can collect it
    // by manually storing KType -> Class mappings.
    val kotlinKTypeToClassMap = mutableMapOf<KType, Class<*>>()

    inline fun <reified T: Any> store()
            = kotlinKTypeToClassMap.put(T::class.defaultType, T::class.java)

    fun store(kType: KType, javaClass: Class<*>)
            = kotlinKTypeToClassMap.put(kType, javaClass)

    override fun bindModel(type: KType, req: MutantRequest) : Any {

        return if (req.isJson)
            jsonConverter(type, req)
        else if (req.isForm)
            formParamsConverter(type, req)
        else
            throw Exception("MutantModelBinder does not know how to handle this request")
    }

    private fun jsonConverter(type: KType, req: MutantRequest) : Any {

        val klass = kotlinKTypeToClassMap[type]
                ?: throw Exception("No KType `$type` saved in MutantModelBinder")

        return GsonJsonRenderer.gson.fromJson(req.body, klass as Class<*>)
    }

    fun formParamsConverter(type: KType, req: MutantRequest) : Any {

        val klass = kotlinKTypeToClassMap[type]
                ?: Exception("No KType `$type` saved in MutantModelBinder")

        val jklass = klass as Class<*>
        val noParams = req.params.keys.size

        val primaryCtor = jklass.kotlin.primaryConstructor
                ?: throw Exception("Could not find the primary constructor of `$type`")

        if (primaryCtor.parameters.size != noParams)
            throw Exception("The primary constructor of `$type` takes ${primaryCtor.parameters.size} params, " +
                            "found $noParams")

        val ctorParams = mutableListOf<Any?>()

        primaryCtor.parameters.forEach {
            when (it.type) {
                kotlin.String::class.defaultType    -> ctorParams.add(req.params[it.name].toString())
                kotlin.Boolean::class.defaultType   -> ctorParams.add(req.params[it.name]?.toBoolean())
                kotlin.Int::class.defaultType       -> ctorParams.add(req.params[it.name]?.toInt())
                kotlin.Long::class.defaultType      -> ctorParams.add(req.params[it.name]?.toLong())
                kotlin.Float::class.defaultType     -> ctorParams.add(req.params[it.name]?.toFloat())
                kotlin.Double::class.defaultType    -> ctorParams.add(req.params[it.name]?.toDouble())
                else                                -> log.warn { "Cannot map ctor param, unknown type `${it.type}" }
            }
        }

        return primaryCtor.call(*ctorParams.toTypedArray())
    }
}