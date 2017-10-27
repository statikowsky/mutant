package org.droba.mutantAbominablePasture.views.containers

import org.droba.mutantSpringloadedSupport.reloadScript
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.droba.mutantSpringloadedSupport.liveReloadConnectionImage

fun mainDiv(divContents: DIV.() -> Unit): String {
    return createHTML()
            .html {

                head {
                    title { +"Mutant" }
                    meta(name = "viewport", content = "width=device-width, initial-scale=1")
                    link(rel = "stylesheet", href = "https://unpkg.com/tachyons/css/tachyons.min.css")

                    style {
                        +"""
                        .fixed-eye {
                            position: absolute;
                            left: 15px;
                            bottom: 15px;
                            width: 2em;
                            height: 2em;
                            cursor: pointer;
                        }

                        .trippy {
                            animation: pulse 5s infinite;
                        }

                        @keyframes pulse {
                            0% {
                                fill: #5e2ca5;
                            }

                            50% {
                                fill: #d5008f;
                            }

                            100% {
                                fill: #5e2ca5;
                            }
                        }
                        """
                    }
                }

                body(null) {
                    div("pa4 pa5-ns sans-serif") {
                        divContents()
                    }
                    reloadScript()
                }
            }
}