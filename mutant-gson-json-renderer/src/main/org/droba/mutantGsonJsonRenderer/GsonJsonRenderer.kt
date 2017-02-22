package org.droba.mutantGsonJsonRenderer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.droba.mutant.M
import org.droba.mutant.Mutant
import org.droba.mutant.MutantRequest
import org.droba.mutant.MutantResponse
import kotlin.reflect.KClass
import com.github.salomonbrys.kotson.*

object GsonJsonRenderer {

    val processJsonObjectActionResult = Mutant.process {
        if (it is JsonObject) {
            return@process MutantResponse(
                    contentType = "application/json",
                    content = it.toString()
            )
        }

        return@process null
    }

    val gson : Gson = GsonBuilder().serializeNulls().create()
}

infix fun <T : Any> MutantRequest.into(klass: KClass<T>) : T
        = GsonJsonRenderer.gson.fromJson(body, klass.java)

inline fun <reified T : Any> MutantRequest.into() : T
        = GsonJsonRenderer.gson.fromJson<T>(body)

infix fun <T: Any> String.into(klass: KClass<T>) : T
        = GsonJsonRenderer.gson.fromJson(this, klass.java)

inline fun <reified T : Any> String.into() : T
        = GsonJsonRenderer.gson.fromJson<T>(this)

fun M.json(model: Any) = MutantResponse(
        contentType = "application/json",
        content = GsonJsonRenderer.gson.toJson(model)
)