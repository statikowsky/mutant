package org.droba.mutantControllerDiscovery

import com.google.common.reflect.ClassPath
import mu.KotlinLogging
import org.droba.mutant.Action
import org.droba.mutant.M
import org.droba.mutant.Method
import org.droba.mutant.Mutant
import org.droba.mutantControllerDiscovery.binders.ModelBinder
import org.droba.mutantControllerDiscovery.binders.PathAndQueryParamBinder
import org.droba.mutantControllerDiscovery.binders.mutantBinders.MutantModelBinder
import org.droba.mutantControllerDiscovery.binders.mutantBinders.MutantPathAndQueryParamBinder
import org.droba.mutantControllerDiscovery.binders.mutantBinders.discoverModels
import org.droba.mutantControllerDiscovery.binders.mutantBinders.modelBinder
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import kotlin.jvm.internal.Lambda
import kotlin.reflect.*
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.reflect

fun Mutant.discoverControllersAndModels() {
    ControllerDiscovery(this, modelBinder { discoverModels() }, MutantPathAndQueryParamBinder())
            .discoverControllers()
}

fun Mutant.discoverControllers(modelConfig: MutantModelBinder.() -> Unit = {}) {
    ControllerDiscovery(this, modelBinder { modelConfig() }, MutantPathAndQueryParamBinder())
            .discoverControllers()
}

fun Mutant.discoverControllers(modelBinder: ModelBinder, pathAndQueryParamBinder: PathAndQueryParamBinder) {
    ControllerDiscovery(this, modelBinder, pathAndQueryParamBinder)
            .discoverControllers()
}

class ControllerDiscovery(
        val mutant: Mutant,
        val modelBinder: ModelBinder,
        val pathAndQueryParamBinder: PathAndQueryParamBinder
) {

    private val log = KotlinLogging.logger {}

    val skipMethods             = listOf("equals", "hashCode", "toString")
    val reservedMembers         = listOf("index", "get", "delete", "update", "create")
    val controllerAnnotations   = listOf(Get::class, Post::class, Delete::class, Put::class, Patch::class)

    fun discoverControllers() {

        log.info { "Running controller discovery!" }

        val cp = ClassPath.from(ClassLoader.getSystemClassLoader())
        val controllersPackages = cp.topLevelClasses
                .filter { it.name.contains("controller") }
                .distinctBy { it.packageName }
                .sortedBy { it.packageName.length }

        log.debug { "Found packages: ${controllersPackages.joinToString { it.packageName }}" }

        val controllerPackage = controllersPackages.first()

        val refs = Reflections(controllerPackage.packageName, SubTypesScanner(false))
        refs.allTypes.forEach { log.debug{ "Found type: $it" } }

        refs.getSubTypesOf(Object::class.java)
                .forEach {
                    val kClass = it.kotlin

                    log.info { "~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~" }
                    log.info { "Controller [${kClass.qualifiedName}], inspecting" }

                    val controllerPath = getControllerPath(kClass, controllerPackage.packageName)

                    log.info { "Additional path (from root) [$controllerPath]" }
                    log.debug { "Controller annotations: [${kClass.annotations}]" }

                    inspectObjectPropertiesForRoutes(kClass, controllerPath)
                }

        log.info("Controller discovery complete.")
    }

    private fun inspectObjectPropertiesForRoutes(kClass: KClass<out Any>, controllerPath: String) {

        val isSingular = kClass.annotations.any { it.annotationClass == Singular::class }

        log.info { "Is controller singular: $isSingular" }

        log.info { "Inspecting object ${kClass.qualifiedName} properties for routes" }

        val instance = getKClassInstance(kClass) ?: return

        kClass.memberProperties
                .filterNot { skipMethods.contains(it.name) }
                .forEach memberIter@ {
                    log.info { "- - - - - - - - - - - - -" }
                    log.info { "Member [${it.name}], inspecting." }
                    log.debug { "Annotations ${it.annotations}" }

                    if (!reservedMembers.contains(it.name)
                            && !it.annotations.map { it.annotationClass }.intersect(controllerAnnotations).any()) {
                        log.warn("This property is not named 'create', 'get', 'index', 'update', 'delete' nor")
                        log.warn("does it have an valid controller annotation (@Get, @Post, @Delete, @Put)")
                        log.warn("so we are skipping it.")

                        return@memberIter
                    }

                    log.debug("Type of return value is {}", it.returnType)

                    val actionLambda = it.call(instance)

                    if (actionLambda == null) {
                        log.warn("Skipping member as we could not get a result.")
                        displayWrongReturnTypeWarning()
                        return@memberIter
                    }

                    val lambdaIntrospect : KFunction<*>? = (actionLambda as Lambda).reflect()

                    if (lambdaIntrospect == null) {
                        log.error { "Controller discovery was unable to introspect your lambda for property ${it.name}"  }
                        return@memberIter
                    }

                    if (lambdaIntrospect.parameters.isEmpty()) {
                        log.error { "Incorrect lambda type. This lambda does not have a M receiver." }
                        return@memberIter
                    }

                    if (lambdaIntrospect.parameters[0].type != M::class.starProjectedType) {
                        log.error { "Incorrect lambda type. This lambda does not have a M receiver." }
                        return@memberIter
                    }

                    val action = if (lambdaIntrospect.parameters.size > 1) {
                        MultiparamActionUtil.wrapWithBindingAction(actionLambda, lambdaIntrospect, modelBinder, pathAndQueryParamBinder)
                    }
                    else {
                        try {
                            @Suppress("UNCHECKED_CAST")
                            actionLambda as Action
                        } catch (e: Exception) {
                            log.warn { "Skipping member as we could not cast the return type to M.() -> Any (Action)" }
                            displayWrongReturnTypeWarning()
                            log.error ("Encountered exception while trying to cast to M.() -> Any (Action)", e )
                            return@memberIter
                        }
                    }

                    val route = "/" + controllerPath + kClass.simpleName?.decapitalize()

                    log.info("Adding member [{}] to route [{}]", it.name, route)

                    val routePathAnnotation = it.annotations
                            .find { it -> it.annotationClass == Path::class }
                            as? Path

                    val routeOverride = if (routePathAnnotation != null)
                        route + routePathAnnotation.path
                    else
                        null

                    if (isSingular)
                        addSingular(mutant, it, routeOverride, route, action)
                    else
                        addPlural(mutant, it, routeOverride, route, action)
                }
    }

    private fun addSingular(mutant: Mutant, it: KCallable<*>, routeOverride: String?, route: String, action: Action) {
        when (it.name) {
            "get"       -> mutant.registerRoute(Method.GET,    routeOverride ?: route, action)
            "create"    -> mutant.registerRoute(Method.POST,   routeOverride ?: route, action)
            "delete"    -> mutant.registerRoute(Method.DELETE, routeOverride ?: route, action)
            "update"    -> mutant.registerRoute(Method.PUT,    routeOverride ?: route, action)
            else        -> addMethod(mutant, it, routeOverride ?: route + "/" + it.name, action)
        }
    }

    private fun addPlural(mutant: Mutant, it: KCallable<*>, routeOverride: String?, route: String, action: Action) {
        when (it.name) {
            "index"     -> mutant.registerRoute(Method.GET,    routeOverride ?: route, action)
            "get"       -> mutant.registerRoute(Method.GET,    routeOverride ?: route + "/:id", action)
            "create"    -> mutant.registerRoute(Method.POST,   routeOverride ?: route, action)
            "delete"    -> mutant.registerRoute(Method.DELETE, routeOverride ?: route + "/:id", action)
            "update"    -> mutant.registerRoute(Method.PUT,    routeOverride ?: route + "/:id", action)
            else        -> addMethod(mutant, it, routeOverride ?: route + "/" + it.name, action)
        }
    }

    private fun addMethod(mutant: Mutant, it: KCallable<*>, route: String, action: Action) {
        if (it.annotations.isEmpty()) {
            log.warn("Skipping member as this member has no annotations.")
            log.warn("You probably want to annotate this member ")
            return
        }

        it.annotations
                .filter { it -> it.annotationClass != Path::class }
                .forEach {
            log.debug("Has annotation {}", it.annotationClass.simpleName)
            log.info("Non resty auto route added! [{}]", route)
            when(it.annotationClass.simpleName) {
                "Get"       -> mutant.registerRoute(Method.GET,     route, action)
                "Post"      -> mutant.registerRoute(Method.POST,    route, action)
                "Delete"    -> mutant.registerRoute(Method.DELETE,  route, action)
                "Put"       -> mutant.registerRoute(Method.PUT,     route, action)
                "Patch"     -> mutant.registerRoute(Method.PATCH,   route, action)
                else        -> log.error("Could not add this member, unknown annotation :(")
            }
        }
    }

    private fun getControllerPath(kClass: KClass<*>, packageName: String) : String
            = kClass.qualifiedName!!
            .removePrefix(packageName)
            .removeSuffix(kClass.simpleName!!)
            .replaceFirst(".", "")
            .replace(".", "/")

    private fun getKClassInstance(kClass: KClass<out Any>) : Any? {

        val isObject = kClass.objectInstance != null

        return if (isObject) {
            kClass.objectInstance
        } else {

            if (kClass.primaryConstructor?.parameters?.isNotEmpty()!!) {
                log.error { "Class ${kClass.qualifiedName} routes cannot be discovered as it lacks a parameterless constructor." }
                return null
            }

            kClass.primaryConstructor?.call()
        }
    }

    private fun displayWrongReturnTypeWarning() {
        log.warn("Did you accidentally mark a member with @Get, @Post, @Delete or @Put without")
        log.warn("act { } ? (all actionable members must return M.() -> Any)!")
    }
}
