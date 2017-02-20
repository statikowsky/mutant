package org.droba.mutant

import org.droba.mutant.Method.*
import org.droba.mutant.dummy.DummyRouter
import org.droba.mutant.pluggables.MutantRouter
import org.slf4j.LoggerFactory

enum class Method { GET, POST, DELETE, PUT, HEAD, OPTIONS, TRACE }

data class M(val req: MutantRequest)

data class Message(val message: String, val details: String, val stackTrace: String? = null)

private val log = LoggerFactory.getLogger(Mutant::class.java)

class Mutant {

    companion object {
        fun process(processor: (Any) -> MutantResponse?) : (Any) -> MutantResponse? = processor
        fun act(action: M.() -> Any) : M.() -> Any = action

        fun setup(mutantConfig: Mutant.() -> Unit) : Mutant {

            val mutant = Mutant()

            mutant.mutantConfig()
            mutant.validateMutantConfig()

            return mutant
        }
    }

    val handler = { req: MutantRequest -> this.handle(req) }
    var router : MutantRouter = DummyRouter()
    var onError: M.(Message) -> MutantResponse
            = { message -> MutantResponse(content = "There was an error with your application.", responseCode = 500) }
    var on404: M.(Message) -> MutantResponse
            = { message -> MutantResponse(content = "404", responseCode = 404) }

    private val resultConverters = mutableListOf<(Any) -> MutantResponse?>()

    fun addResultConverter(resultConverter: (Any) -> MutantResponse?) = resultConverters.add(resultConverter)

    fun renderMessage(handle: M.(Message) -> MutantResponse) : M.(Message) -> MutantResponse = handle

    fun validateMutantConfig() {
        if (router is DummyRouter) throw RuntimeException("In mutant setup you MUST specify a router.")
    }

    fun get     (route: String, action: M.() -> Any) = registerRoute(GET, route, action)
    fun post    (route: String, action: M.() -> Any) = registerRoute(POST, route, action)
    fun delete  (route: String, action: M.() -> Any) = registerRoute(DELETE, route, action)
    fun put     (route: String, action: M.() -> Any) = registerRoute(PUT, route, action)
    fun head    (route: String, action: M.() -> Any) = registerRoute(HEAD, route, action)
    fun options (route: String, action: M.() -> Any) = registerRoute(OPTIONS, route, action)
    fun trace   (route: String, action: M.() -> Any) = registerRoute(TRACE, route, action)

    fun registerRoute(method: Method, route: String, action: M.() -> Any) {
        log.debug("Registering route: {} {} -> {}", method, route, action.javaClass)
        router.registerRoute(method, route, action)
    }

    fun handle(req: MutantRequest) : MutantResponse {
        try {

            log.info("> [${req.method}] ${req.path}")

            val action = router.matchToAction(req)

            val actionResult = action(M(req))

            return convertResult(actionResult)

        } catch(e: Exception) {
            return onException(req, e)
        }
    }

    private fun onException(req: MutantRequest, e: Exception): MutantResponse {

        val errorMessage = Message(
                "Type ${e.javaClass.name}",
                e.message ?: "",
                e.stackTrace.joinToString("\n"))

        //TODO: if not running debug do not respond with error message!
        if (e is MutantHalt) {

            log.debug("Mutant halt with status:${e.status} and details:${e.description}", e)

            if (e.status != 200) {
                log.error("Mutant halt error", e)
                return onError(M(req), errorMessage)
            } else {
                return MutantResponse(
                        responseCode = e.status,
                        content = e.description
                )
            }
        }
        else if (e is MutantRouteNotFound) {
            val message = Message("404", "No action found for route [${e.route}] !")
            return on404(M(req), message)
        }
        else {
            log.error("Mutant error", e)
            return onError(M(req), errorMessage)
        }
    }

    private fun convertResult(actionResult: Any) : MutantResponse
        = when(actionResult) {
            is MutantResponse -> actionResult
            is Unit -> MutantResponse()
            is String -> MutantResponse(
                    contentType = "text/html",
                    content = actionResult
            )
            else -> runResultConverters(actionResult)
        }

    private fun runResultConverters(actionResult: Any): MutantResponse {
        resultConverters.forEach {
            it(actionResult)?.let { response -> return response }
        }

        throw MutantHalt("Mutant does not understand you action result :( (${actionResult.javaClass})")
    }
}