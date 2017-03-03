package org.droba.mutantJettyEngine

import org.droba.mutant.Method
import org.droba.mutant.MutantRequest
import org.droba.mutant.pluggables.MutantSession
import org.eclipse.jetty.server.Request
import java.util.stream.Collectors
import javax.servlet.http.Cookie

class MutantJettyRequest(val request: Request) : MutantRequest() {

    init {
        request.characterEncoding = "UTF-8"
        request.characterEncoding = "UTF-8"
    }

    override val ip: String
        get() = request.remoteAddr

    override val host: String
        get() = request.remoteHost

    override val method: Method by lazy {
        when(request.method) {
            "GET"       -> Method.GET
            "POST"      -> Method.POST
            "PUT"       -> Method.PUT
            "DELETE"    -> Method.DELETE
            "PATCH"     -> Method.PATCH
            "HEAD"      -> Method.HEAD
            "OPTIONS"   -> Method.OPTIONS
            "TRACE"     -> Method.TRACE
            else -> throw Exception("Could not map request method")
        }
    }

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

    override val queryParams: Map<String, String>
        get() = TODO("not implemented")

    override val queryMultiParams: Map<String, List<String>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val formParams: Map<String, String>
        get() = TODO("not implemented")

    override val formMultiParams: Map<String, List<String>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val contentLength: Int
        get() = request.contentLength

    override val contentType: String
        get() = request.contentType ?: ""

    override val session: MutantSession
        get() = throw Exception("Sessions be dead")

    override val isJson by lazy { !contentType.isNullOrBlank() && contentType.startsWith("application/json") }

    override val isForm by lazy { contentType == "x-www-form-urlencoded" }

    override var pathParams = mapOf<String, String>()
}
