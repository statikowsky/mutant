package org.droba.mutantJettyEngine

import org.droba.mutant.Halo
import org.droba.mutant.pluggables.HaloEngine
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.websocket.server.WebSocketHandler

class MutantJettyServer : HaloEngine {

    override fun startup(host: String, port: Int, halo: Halo) {

        val server = Server()

        val http = ServerConnector(server)
        http.host = host
        http.port = port

        server.addConnector(http)

        val mainHandler = HandlerList()

        val websocketHandler = MutantJettyWebSocketHandler(halo)
        val mutantHandler = MutantJettyHandler(halo)

        mainHandler.addHandler(websocketHandler)
        mainHandler.addHandler(mutantHandler)

        server.handler = mainHandler

        server.start()
        server.join()
    }
}