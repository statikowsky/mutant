package org.droba.mutantJettyEmbeddedServlet

import org.droba.mutant.Method
import org.droba.mutant.MutantRequest
import java.util.stream.Collectors
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

class JettyMutantRequest(val request: HttpServletRequest, val reqMethod: Method) : MutantRequest() {

    init {
        request.characterEncoding = "UTF-8"
        request.characterEncoding = "UTF-8"
    }

    override val ip: String
        get() = request.remoteAddr

    override val host: String
        get() = request.remoteHost

    override val method: Method
        get() = reqMethod

    override val protocol: String
        get() = request.protocol

    override val path: String
        get() = request.pathInfo ?: ""

    override val body: String by lazy { request.reader.lines().collect(Collectors.joining()) }

    override val cookies: List<Cookie> by lazy { request.cookies.asList() }

    override val headerNames: List<String> by lazy { request.headerNames.toList() }

    override val headers: Map<String, String> by lazy {
        request.headerNames
                .toList()
                .associateBy({ it }, { request.getHeader(it)})
    }

    override val paramNames: List<String> by lazy { request.parameterNames.toList() }

    override val params: Map<String, String> by lazy {
        request.parameterMap
                .filter { it.value.size == 1 }
                .map { it.key to it.value[0] }
                .toMap()
    }

    override val multiParams: Map<String, List<String>> by lazy {
        request.parameterMap
                .filter { it.value.size > 1 }
                .map { it.key to it.value.toList() }
                .toMap()
    }

    override val attributes: List<String>
        get() = request.attributeNames.toList()

    override val contentLength: Int
        get() = request.contentLength

    override val contentType: String
        get() = request.contentType ?: ""

    override val session: HttpSession
        get() = request.session

    override val isJson by lazy { !contentType.isNullOrBlank() && contentType.startsWith("application/json") }

    override val isForm by lazy { contentType == "x-www-form-urlencoded" }

    override var pathParams = mapOf<String, String>()
}
