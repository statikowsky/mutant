package org.droba.mutantJettyEngine

import mu.KotlinLogging
import org.droba.mutant.Halo
import org.eclipse.jetty.websocket.api.WebSocketAdapter
import org.eclipse.jetty.websocket.server.WebSocketHandler
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse
import org.eclipse.jetty.websocket.servlet.WebSocketCreator
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory

class MutantJettyWebSocketHandler(val halo: Halo) : WebSocketHandler() {

    override fun configure(factory: WebSocketServletFactory?) {
        factory!!.creator = MutantWebSocketCreator(halo)
    }
}

class MutantWebSocketCreator(val halo: Halo) : WebSocketCreator {

    override fun createWebSocket(req: ServletUpgradeRequest?, resp: ServletUpgradeResponse?): Any {
        return MutantWebSocket(halo)
    }
}

class MutantWebSocket(val halo: Halo) : WebSocketAdapter() {

    val log = KotlinLogging.logger {}

    override fun onWebSocketConnect(sess: org.eclipse.jetty.websocket.api.Session?) {
        super.onWebSocketConnect(sess)

        log.info("WS connect: {}", session)

        remote.sendString("You are now connected to MutantWS!")

        halo.WSsendText = { text -> remote.sendString(text) }
    }

    override fun onWebSocketText(message: String?) {
        super.onWebSocketText(message)
        log.info("Received message: {}", message)
    }

    override fun onWebSocketClose(statusCode: Int, reason: String?) {
        super.onWebSocketClose(statusCode, reason)
        log.info("WS closed: {} - {}", statusCode, reason)
    }

    override fun onWebSocketError(cause: Throwable?) {
        super.onWebSocketError(cause)
        log.error("WS error", cause)
    }
}