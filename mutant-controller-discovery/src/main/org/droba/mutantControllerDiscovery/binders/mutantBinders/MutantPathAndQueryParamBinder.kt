package org.droba.mutantControllerDiscovery.binders.mutantBinders

import org.droba.mutant.MutantHalt
import org.droba.mutantControllerDiscovery.binders.PathAndQueryParamBinder
import kotlin.reflect.KType
import kotlin.reflect.defaultType
import kotlin.reflect.full.starProjectedType

class MutantPathAndQueryParamBinder : PathAndQueryParamBinder {

    val binders = mutableMapOf<KType, (String) -> Any>()

    init {
        with(binders) {
            put(String::class.starProjectedType,  { string -> string })
            put(Int::class.starProjectedType,     String::toInt)
            put(Long::class.starProjectedType,    String::toLong)
            put(Float::class.starProjectedType,   String::toFloat)
            put(Double::class.starProjectedType,  String::toDouble)
            put(Boolean::class.starProjectedType, String::toBoolean)
        }
    }

    override fun bindPathParam(value: String, type: KType) : Any {
        val binder = binders[type] ?:
                throw Exception("Could not find appropriate binder for type: $type")

        return try {
            binder.invoke(value)
        } catch (e: Exception) {
            throw MutantHalt("Could not bind path or query param of type $type with value $value")
        }
    }
}