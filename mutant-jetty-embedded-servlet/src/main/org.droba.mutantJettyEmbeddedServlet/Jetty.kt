package org.droba.mutantJettyEmbeddedServlet

import org.droba.mutant.Halo
import org.droba.mutant.pluggables.HaloEngine
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.component.AbstractLifeCycle
import org.eclipse.jetty.util.component.LifeCycle
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer
import org.slf4j.LoggerFactory
import org.stagemonitor.web.WebPlugin
import javax.websocket.server.ServerEndpointConfig

private val log = LoggerFactory.getLogger(Jetty::class.java)

class Jetty : HaloEngine {

    override fun startup(host: String, port: Int, halo: Halo) {
        log.debug("Starting jetty default server (currently defaults to localhost:8080)")
        val server = Server()

        val http = ServerConnector(server)
        http.host = host
        http.port = port

        server.addConnector(http)

        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"
        context.resourceBase = System.getProperty("java.io.tmpdir")

        server.handler = context

        context.addServlet(ServletHolder(MutantServlet(halo)), "/*")

        val staticServletHolder = ServletHolder("res", DefaultServlet::class.java)

        with(staticServletHolder, {
            setInitParameter("resourceBase",    "res")
            setInitParameter("dirAllowed",      "false")
            setInitParameter("pathInfoOnly",    "true")
        })

        context.addServlet(staticServletHolder, "/res/*")

        // set session cookies to http only
        context.sessionHandler.sessionCookieConfig.isHttpOnly = true

        val wsContainer = WebSocketServerContainerInitializer.configureContext(context)
        val endpointConfig = ServerEndpointConfig.Builder
                .create(MutantWebSocket::class.java, "/mutantws")
                .configurator(MutantWebSocketConfigurator(halo))
                .build()

        wsContainer.addEndpoint(endpointConfig)

        server.start()
        server.join()

        log.debug("jetty server started")
    }

}