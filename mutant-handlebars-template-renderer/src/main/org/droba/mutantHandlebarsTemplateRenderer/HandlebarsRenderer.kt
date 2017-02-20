package org.droba.mutantHandlebarsTemplateRenderer

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import org.droba.mutant.M
import org.droba.mutant.MutantResponse
import org.droba.mutantHandlebarsTemplateRenderer.HandlebarsRenderer.render

object HandlebarsRenderer {

    var handlebars: Handlebars

    init {
        val templateLoader = ClassPathTemplateLoader()
        templateLoader.prefix = "/templates"
        handlebars = Handlebars(templateLoader)
    }

    fun render(renderPair: Pair<String, Any>) : String {
        val (template, model) = renderPair
        return handlebars.compile(template).apply(model)
    }
}

fun M.view(renderPair: Pair<String, Any>) = MutantResponse(
        contentType = "text/html",
        content     = render(renderPair)
)

fun M.view(view: String) = MutantResponse(
        contentType = "text/html",
        content     = render(view to Unit)
)