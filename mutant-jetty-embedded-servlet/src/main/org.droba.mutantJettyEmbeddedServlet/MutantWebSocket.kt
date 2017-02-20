package org.droba.mutantJettyEmbeddedServlet

import org.droba.mutant.Halo
import org.slf4j.LoggerFactory
import javax.websocket.*

class MutantWebSocket(val halo: Halo) : Endpoint(), MessageHandler.Whole<String> {

    val log = LoggerFactory.getLogger(MutantWebSocket::class.java)

    var session: Session? = null
    var remote: RemoteEndpoint.Async? = null

    override fun onOpen(session: Session?, config: EndpointConfig?) {
        this.session = session
        this.remote = this.session?.asyncRemote

        log.info("WS connect: {}", session)

        val res = this.remote?.sendText("You are now connected to MutantWS!")

        halo.WSsendText = { text -> this.remote?.sendText(text) }
    }

    override fun onClose(session: Session?, closeReason: CloseReason?) {
        super.onClose(session, closeReason)

        this.session = null
        this.remote = null

        log.info("WS closed: {} - {}", closeReason?.closeCode, closeReason?.reasonPhrase)
    }

    override fun onError(session: Session?, thr: Throwable?) {
        super.onError(session, thr)
        log.warn("WS error", thr)
    }

    override fun onMessage(message: String?) {
        log.info("Received message: {}", message)
    }
}
