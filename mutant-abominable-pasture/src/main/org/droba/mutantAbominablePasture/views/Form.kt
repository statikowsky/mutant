package org.droba.mutantAbominablePasture.views

import kotlinx.html.*
import org.droba.mutantAbominablePasture.views.containers.mainDiv
import kotlin.reflect.KProperty1
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.defaultType

class Thing(val name: String, val surname: String, val age: Int)

object TestForm {
    fun view () : String {
        return mainDiv {
            h1 { +"Form" }
            div {
                p { +"Hello "}
                formFor<Thing>("/test-not-working")
            }
        }
    }
}

inline fun <reified T: Any> FlowContent.formFor(uri: String) {

    val memberProperties = T::class.declaredMemberProperties

    form {
        action = uri
        method = FormMethod.get

        label {
            for_ = "valueName"
            +"valueName"
        }

        input {
            id = "valueName"
            name = "valueName"
            type = InputType.text
        }

        input {
            type = InputType.submit
            value = "Save"
        }

        memberProperties.forEach {
            generateFormItem(it)
        }
    }
}

fun <T> generateFormItem(it: KProperty1<T, *>) {

    when(it.returnType) {
        String::class.defaultType -> generateStringFormItem(it)
        Number::class.defaultType -> generateNumberFormItem(it)
        Collection::class.defaultType -> whatDoWeDoNow(it)
    }
}

fun <T> whatDoWeDoNow(it: KProperty1<T, *>) {
    println("Dunno")
}

fun <T> generateNumberFormItem(it: KProperty1<T, *>) {
    println("It's a number!")
}

fun <T> generateStringFormItem(it: KProperty1<T, *>) {
    println("It's a string!")
}




