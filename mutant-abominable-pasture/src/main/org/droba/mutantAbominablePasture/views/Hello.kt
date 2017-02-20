package org.droba.mutantAbominablePasture.views

import kotlinx.html.*
import org.droba.mutantAbominablePasture.views.containers.mainDiv

object Hello {

    fun view () : String {

        val genMenuItem : DL.(String, String, String) -> Unit = { url , name, description ->
            dt("f5 mt3") {
                a {
                    href = url
                    classes = setOf("link", "underline", "blue", "hover-hot-pink")
                    +name
                }
            }
            dd("ma0 m10 lh-copy black-70") {
                +"⇒ $description"
            }
        }

        return mainDiv {
            h1("f-headline code blue") { +"Hello" }

            h1("lh-copy black-70") { +"Welcome to Mutant!" }

            div("dt") {

                div("dtc v-mid") {
                    img {
                        classes = setOf("mw5")
                        src = "data:image/svg+xml;utf8;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iaXNvLTg4NTktMSI/Pgo8IS0tIEdlbmVyYXRvcjogQWRvYmUgSWxsdXN0cmF0b3IgMTguMS4xLCBTVkcgRXhwb3J0IFBsdWctSW4gLiBTVkcgVmVyc2lvbjogNi4wMCBCdWlsZCAwKSAgLS0+CjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgdmVyc2lvbj0iMS4xIiBpZD0iQ2FwYV8xIiB4PSIwcHgiIHk9IjBweCIgdmlld0JveD0iMCAwIDMyIDMyIiBzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCAzMiAzMjsiIHhtbDpzcGFjZT0icHJlc2VydmUiIHdpZHRoPSIzMnB4IiBoZWlnaHQ9IjMycHgiPgo8Zz4KCTxnIGlkPSJsYWJfMV8iPgoJCTxwYXRoIGQ9Ik0yMC42ODIsMy43MzJDMjAuMjA5LDMuMjYsMTkuNTgzLDMsMTguOTE0LDNzLTEuMjk1LDAuMjYtMS43NywwLjczM2wtMS40MSwxLjQxMiAgICBDMTUuMjYxLDUuNjE3LDE1LDYuMjQ1LDE1LDYuOTE0YzAsMC40NzEsMC4xMjksMC45MjIsMC4zNzEsMS4zMTNMMS43OTUsMTMuNjY2Yy0wLjkwOCwwLjM5OS0xLjU1OSwxLjIxOC0xLjc0MiwyLjE4OSAgICBjLTAuMTg1LDAuOTc3LDAuMTI1LDEuOTc5LDAuODM0LDIuNjg3bDEyLjcyLDEyLjU4YzAuNTQ4LDAuNTQ4LDEuMjc2LDAuODU5LDIuMDQ1LDAuODc3QzE1LjY2OSwzMiwxNS43MTIsMzIsMTUuNzI5LDMyICAgIGMwLjIwMiwwLDAuNDA3LTAuMDIxLDAuNjEtMC4wNjJjMC45OTQtMC4yMDYsMS44MDgtMC44OTMsMi4xNzctMS44MjhsNS4zNDItMTMuMzc2YzAuNDAyLDAuMjY1LDAuODc1LDAuNDA3LDEuMzY3LDAuNDA3ICAgIGMwLjY3LDAsMS4yOTctMC4yNjEsMS43NjgtMC43MzNMMjguNCwxNWMwLjQ3Ny0wLjQ3NCwwLjczOC0xLjEwMywwLjczOC0xLjc3M3MtMC4yNjItMS4zLTAuNzMyLTEuNzY4TDIwLjY4MiwzLjczMnogICAgIE0xNi42NiwyOS4zNjdjLTAuMTI0LDAuMzEzLTAuMzk3LDAuNTQ0LTAuNzI3LDAuNjEyYy0wLjA3NiwwLjAxNi0wLjE1MywwLjAyMi0wLjIyOSwwLjAyMWMtMC4yNTQtMC4wMDYtMC40OTktMC4xMDgtMC42ODItMC4yOTIgICAgTDIuMjk0LDE3LjEyYy0wLjIzNC0wLjIzMy0wLjMzNy0wLjU2Ny0wLjI3NS0wLjg5M2MwLjA2MS0wLjMyNCwwLjI3OS0wLjU5OCwwLjU4Mi0wLjczbDYuMjE3LTIuNDkgICAgYzQuMTg5LDEuMzkzLDguMzc5LDAuMDUxLDEyLjU3LDQuNTIyTDE2LjY2LDI5LjM2N3ogTTI2Ljk5MywxMy41OGwtMS40MTQsMS40MTNjLTAuMTk1LDAuMTk2LTAuNTEyLDAuMTk2LTAuNzA3LDBsLTEuNzY4LTEuNzY3ICAgIGwtMS40MzIsMy41ODlsMC4xMTktMC4zMDNjLTMuMDEtMy4wMDUtNi4wNjktMy4zODQtOC44MjktMy43MjNjLTAuODg3LTAuMTA5LTEuNzQ3LTAuMjIzLTIuNTkyLTAuNDA1bDguNDkxLTMuNDAxbC0xLjcxNS0xLjcxNSAgICBjLTAuMTk1LTAuMTk1LTAuMTk1LTAuNTEyLDAtMC43MDdsMS40MTQtMS40MTVjMC4xOTUtMC4xOTUsMC41MTItMC4xOTUsMC43MDcsMGw3LjcyNSw3LjcyNyAgICBDMjcuMTg5LDEzLjA2OCwyNy4xODksMTMuMzg1LDI2Ljk5MywxMy41OHoiIGZpbGw9IiNmZjQxYjQiLz4KCQk8cGF0aCBkPSJNMTYuNSwyMWMxLjM3OCwwLDIuNS0xLjEyMSwyLjUtMi41UzE3Ljg3OSwxNiwxNi41LDE2UzE0LDE3LjEyMSwxNCwxOC41UzE1LjEyMiwyMSwxNi41LDIxeiAgICAgTTE2LjUsMTdjMC44MjgsMCwxLjUsMC42NzIsMS41LDEuNVMxNy4zMjgsMjAsMTYuNSwyMGMtMC44MjksMC0xLjUtMC42NzItMS41LTEuNVMxNS42NzEsMTcsMTYuNSwxN3oiIGZpbGw9IiNmZjQxYjQiLz4KCQk8cGF0aCBkPSJNMjkuNSwwQzI4LjEyMiwwLDI3LDEuMTIxLDI3LDIuNVMyOC4xMjIsNSwyOS41LDVTMzIsMy44NzksMzIsMi41UzMwLjg3OSwwLDI5LjUsMHogTTI5LjUsNCAgICBDMjguNjczLDQsMjgsMy4zMjgsMjgsMi41UzI4LjY3MywxLDI5LjUsMVMzMSwxLjY3MiwzMSwyLjVTMzAuMzI4LDQsMjkuNSw0eiIgZmlsbD0iI2ZmNDFiNCIvPgoJCTxwYXRoIGQ9Ik04LDE3YzAsMS4xMDMsMC44OTcsMiwyLDJzMi0wLjg5NywyLTJzLTAuODk3LTItMi0yUzgsMTUuODk3LDgsMTd6IE0xMCwxNmMwLjU1MiwwLDEsMC40NDcsMSwxICAgIHMtMC40NDgsMS0xLDFzLTEtMC40NDctMS0xUzkuNDQ4LDE2LDEwLDE2eiIgZmlsbD0iI2ZmNDFiNCIvPgoJCTxjaXJjbGUgY3g9IjEzIiBjeT0iMjMiIHI9IjEiIGZpbGw9IiNmZjQxYjQiLz4KCQk8Y2lyY2xlIGN4PSIyOSIgY3k9IjgiIHI9IjEiIGZpbGw9IiNmZjQxYjQiLz4KCTwvZz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8Zz4KPC9nPgo8L3N2Zz4K"
                    }
                }

                div("dtc v-mid pl3") {
                    p("lh-copy hot-pink") {
                        +"Mutant is a homebrew project. Stuff might go boom!"
                    }
                }
            }

            p("lh-copy") {
                +"That said, have fun playing around! For an overview check out the "
                a {
                    href = "http://stg1.droba.org"
                    classes = setOf("link", "underline", "blue", "hover-hot-pink")
                    +"docs"
                }
            }

            dl("lh-copy ph4 mt0"){
                genMenuItem(
                        "/pasture",
                        "pasture",
                        "a small simple crudy app"
                )
                genMenuItem(
                        "/eye",
                        "eye",
                        "mutant diagnostics"
                )
                genMenuItem(
                        "/error",
                        "error",
                        "a simple test error"
                )
                genMenuItem(
                        "/is-this-the-right-place",
                        "404",
                        "default, but you can always set your own"
                )
            }
        }
    }
}

