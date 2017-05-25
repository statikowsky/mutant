package org.droba.mutantJettyEngine

import org.droba.mutant.Halo
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MutantJettyHandler(val halo: Halo) : AbstractHandler() {

    override fun handle(target: String?,
                        baseRequest: Request?,
                        request: HttpServletRequest?,
                        res: HttpServletResponse?) {

            val mutantRequest = MutantJettyRequest(baseRequest!!)

            val mutRes = halo.handler!!.invoke(mutantRequest)

            res!!.contentType = mutRes.contentType
            res.status        = mutRes.responseCode
            
            mutRes.headers
                    .forEach { res.setHeader(it.key, it.value) }

            res.writer.write(mutRes.content)

            baseRequest.isHandled = true
    }
}