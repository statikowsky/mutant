package org.droba.mutantNaiveRouter

import org.droba.mutant.*
import org.droba.mutant.pluggables.MutantRouter
import org.slf4j.LoggerFactory
import java.util.*

class NaiveRouter : MutantRouter {

    data class MutantRoute(
            val method: Method,
            val route: String,
            val pathParamNames: List<String>? = null
    )

    private val log = LoggerFactory.getLogger(MutantRouter::class.java)
    private val routes = LinkedHashMap<MutantRoute, Action>()

    override fun registerRoute(method: Method, route: String, action: Action) {
        val mutantRoute = mapToMutantRoute(method, route)
        routes.put(mutantRoute, action)
    }

    override fun matchToAction(req: MutantRequest): Action
        = checkStaticRoutes(req)
            ?: checkDynamicRoutes(req)

    fun checkStaticRoutes(req: MutantRequest) : Action? {

        val method = req.method
        val route = req.path

        val match = routes[MutantRoute(method, route)]

        if (match != null) {
            log.info("Found static handle {}.", route)
            return match
        }

        log.debug("No static handle found.")
        return null
    }

    fun checkDynamicRoutes(req: MutantRequest) : Action {

        val method = req.method
        val route = req.path

        for ((mutantRoute, action) in routes) {

            log.trace("checking dynamic: {}", mutantRoute.route)

            val routeRegex = mutantRoute.route.toRegex()

            if (method == mutantRoute.method
                    && route.matches(routeRegex)) {
                log.info("Found dynamic handle {}", mutantRoute.route)

                // get dynamic pathParamsWithValues
                val pathParamsWithValues = linkedMapOf<String, String>()

                routeRegex.findAll(route, 0).forEach {
                    for(i in 1..it.groups.size-1) {

                        val bindingName = mutantRoute.pathParamNames?.get(i-1)
                        val bindingValue = it.groups[i]?.value
                        if (bindingName != null && bindingValue != null) {
                            log.trace("Found binding: [{},{}]", bindingName, bindingValue)
                            pathParamsWithValues.put(bindingName, bindingValue)
                        }
                    }
                }

                req.pathParams = pathParamsWithValues

                return action
            }
        }

        log.debug("No dynamic handle found.")
        throw MutantRouteNotFound(route)
    }

    private fun mapToMutantRoute(method: Method, route: String) : MutantRoute {
        if (!route.contains(":")) {
            log.trace("Route type: static")
            return MutantRoute(method, route)
        }
        else {
            log.trace("Route type: dynamic")

            val m = ":(\\w+)".toRegex().toPattern().matcher(route)
            val pathParamNames = ArrayList<String>()

            while(m.find()) {
                pathParamNames.add(m.group())
                log.trace("Found path param: {}", m.group())
            }

            return MutantRoute(
                    method,
                    route.replace(":(\\w+)".toRegex(), """([^/]+)"""),
                    pathParamNames)
        }
    }
}
