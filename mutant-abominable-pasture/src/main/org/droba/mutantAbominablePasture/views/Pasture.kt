package org.droba.mutantAbominablePasture.views

import kotlinx.html.*
import org.droba.mutantAbominablePasture.views.containers.mainDiv

object PastureViews {

    fun login() = mainDiv {

        div ("measure center") {
            h1("purple") { +"Abominable Pasture" }
        }

        form {
            classes = setOf("measure center")
            method = FormMethod.post

            fieldSet {
                id = "login"
                classes = setOf("ba b--transparent ph0 mh0")

                legEnd("f4 fw6 ph0 mh0") { + "Sign in"}

                div("mt3") {
                    label("db fw6 lh-copy f6") {
                        for_ = "email-address"
                        +"Email"
                    }

                    input {
                        classes = setOf("pa2", "input-reset", "ba", "b--light-gray", "w-100")
                        type = InputType.email
                        name = "email-address"
                        id = "email-address"
                    }
                }

                div("mv3") {
                    label("db fw6 lh-copy f6") {
                        for_ = "password"
                        +"Password"
                    }
                    input {
                        classes = setOf("pa2", "input-reset", "ba", "b--light-gray", "w-100")
                        type = InputType.password
                        name = "password"
                        id = "password"
                    }
                }
            }

            div {
                input(classes = "b ph3 pv2 input-reset ba b--black bg-transparent grow pointer f6 dib") {
                    type = InputType.submit
                    value = "Sign in"
                }
            }

            div("mv5 lh-copy sans-serif") {
                +"Try these (email / password):"
                ul {
                    li { +"admin@ap.com / admin" }
                    li { +"user@ap.com / user" }
                }
            }
        }
    }
}
