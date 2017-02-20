package org.droba.mutant

import javax.servlet.http.Cookie
import javax.servlet.http.HttpSession

abstract class MutantRequest {

    abstract val ip: String
    abstract val host: String
    abstract val method: Method
    abstract val protocol: String
    abstract val path: String

    abstract val body: String
    abstract val cookies: List<Cookie>

    abstract val headerNames: List<String>
    abstract val headers: Map<String, String>

    abstract val paramNames: List<String>
    abstract val params: Map<String, String>
    abstract val multiParams: Map<String, List<String>>

    abstract val attributes: List<String>

    abstract val contentLength: Int
    abstract val contentType: String

    abstract val session: HttpSession

    abstract val isJson: Boolean
    abstract val isForm: Boolean

    abstract var pathParams: Map<String, String>

    val extra = mapOf<Any, Any>()
}
