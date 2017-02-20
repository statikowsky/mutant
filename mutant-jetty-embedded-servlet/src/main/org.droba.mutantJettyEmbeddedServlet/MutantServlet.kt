package org.droba.mutantJettyEmbeddedServlet

import com.github.salomonbrys.kodein.conf.KodeinGlobalAware
import com.github.salomonbrys.kodein.instance
import org.droba.mutant.Halo
import org.droba.mutant.Method
import org.droba.mutant.Method.*
import org.droba.mutant.Mutant
import org.droba.mutant.MutantResponse
import javax.servlet.http.*

class MutantServlet(val halo: Halo) : HttpServlet(), KodeinGlobalAware {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, GET)

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, POST)

    override fun doDelete(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, DELETE)

    override fun doPut(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, PUT)

    override fun doOptions(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, OPTIONS)

    override fun doTrace(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, TRACE)

    override fun doHead(req: HttpServletRequest?, resp: HttpServletResponse?)
            = haloHandle(req!!, resp!!, HEAD)

    private fun haloHandle(req: HttpServletRequest, res: HttpServletResponse, method: Method) {

        val mutantRequest  = JettyMutantRequest(req, method)
        val mutantResponse = halo.handler!!.invoke(mutantRequest)

        transform(res, mutantResponse)
    }

    fun transform(res: HttpServletResponse, mutRes: MutantResponse): Unit {

        res.contentType = mutRes.contentType
        res.status      = mutRes.responseCode

        mutRes.headers.forEach { res.setHeader(it.key, it.value) }

        res.writer.write(mutRes.content)
    }
}
