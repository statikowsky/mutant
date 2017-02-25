package org.droba.mutantControllerDiscovery.binders.mutantBinders

import org.droba.mutant.MutantHalt
import org.droba.mutantControllerDiscovery.binders.PathAndQueryParamBinder
import kotlin.reflect.KType
import kotlin.reflect.defaultType

class MutantPathAndQueryParamBinder : PathAndQueryParamBinder {

    val binders = mutableMapOf<KType, (String) -> Any>()

    init {
        binders.put(String::class.defaultType,  { string -> string })
        binders.put(Int::class.defaultType,     String::toInt)
        binders.put(Long::class.defaultType,    String::toLong)
        binders.put(Float::class.defaultType,   String::toFloat)
        binders.put(Double::class.defaultType,  String::toDouble)
        binders.put(Char::class.defaultType,    String::toDouble)
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