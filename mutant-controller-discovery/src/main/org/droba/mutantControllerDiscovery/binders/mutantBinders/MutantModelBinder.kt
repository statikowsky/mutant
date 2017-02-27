package org.droba.mutantControllerDiscovery.binders.mutantBinders

import org.droba.mutant.MutantRequest
import org.droba.mutantControllerDiscovery.binders.ModelBinder
import org.droba.mutantGsonJsonRenderer.GsonJsonRenderer
import kotlin.reflect.KType
import kotlin.reflect.defaultType

fun modelBinder (modelConfig: MutantModelBinder.() -> Unit) : MutantModelBinder {
    val modelBinder = MutantModelBinder()
    modelBinder.modelConfig()
    return modelBinder
}

class MutantModelBinder : ModelBinder {

    // Kotlin reflection does not yet support full reflection
    // over lambdas - specifically getting the parameters KType's
    // java class.
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
        else
            pathConverter(type, req)
    }

    private fun jsonConverter(type: KType, req: MutantRequest) : Any {
        val klass = kotlinKTypeToClassMap[type]
                ?: Exception("No KType saved in MutantModelBinder")

        return GsonJsonRenderer.gson.fromJson(req.body, klass as Class<*>)
    }

    fun pathConverter(type: KType, req: MutantRequest) : Any
            = throw NotImplementedError("Wah wah")
}