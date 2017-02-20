package org.droba.mutantControllerDiscovery

import org.slf4j.LoggerFactory
import kotlin.reflect.KCallable

import com.google.common.reflect.ClassPath
import org.droba.mutant.M
import org.droba.mutant.Method
import org.droba.mutant.Mutant
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import kotlin.reflect.KClass

object ControllerDiscovery {

    val log = LoggerFactory.getLogger(ControllerDiscovery.javaClass)

    val skipMethods = listOf("equals", "hashCode", "toString")
    val reservedMembers = listOf("index", "get", "delete", "update", "create")
    val controllerAnnotations = listOf(Get::class, Post::class, Delete::class, Put::class)

    fun Mutant.discoverControllers() {
        log.info("Running controller discovery!")

        val cp = ClassPath.from(ClassLoader.getSystemClassLoader())
        val controllersPackages = cp.topLevelClasses
                .filter { it.name.contains("controller") }
                .distinctBy { it.packageName }
                .sortedBy { it.packageName.length }

        log.debug("Found packages: {}", controllersPackages.joinToString { it.packageName })

        val controllerPackage = controllersPackages.first()

        val refs = Reflections(controllerPackage.packageName, SubTypesScanner(false))
        refs.allTypes.forEach { log.debug("Found type: {}", it) }

        refs.getSubTypesOf(Object::class.java)
                .forEach {
                    val kClass = it.kotlin

                    log.info("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~")
                    log.info("Controller [{}], inspecting", kClass.qualifiedName)

                    val controllerPath = kClass.qualifiedName!!
                            .removePrefix(controllerPackage.packageName)
                            .removeSuffix(kClass.simpleName!!)
                            .replaceFirst(".", "")
                            .replace(".", "/")

                    log.info("Additional path (from root) [{}]", controllerPath)
                    log.debug("Controller annotations: [{}]", kClass.annotations)

                    val isSingular = kClass.annotations.map{ it.annotationClass }.contains(Singular::class)

                    log.info("Is controller singular: {}", isSingular)

                    inspectMembersForRoutes(this, kClass, isSingular, controllerPath)
                }

        log.info("Controller discovery complete.")
    }

    fun inspectMembersForRoutes(mutant: Mutant, kClass: KClass<out Any>, isSingular: Boolean, controllerPath: String) {
        kClass.members
                .filterNot { skipMethods.contains(it.name) }
                .forEach memberIter@ {
                    log.info("- - - - - - - - - - - - -")
                    log.info("Member [{}], inspecting.", it.name)
                    log.debug("Annotations {}", it.annotations)

                    if (!reservedMembers.contains(it.name)
                            && !it.annotations.map { it.annotationClass }.intersect(controllerAnnotations).any()) {
                        log.info("This member is not named 'create', 'get', 'index', 'update', 'delete' nor")
                        log.info("does it have an valid controller annotation (@Get, @Post, @Delete, @Put)")
                        log.info("so we are skipping it.")

                        val annotations = it.annotations.map { it.annotationClass }
                        annotations.forEach { log.debug(it.simpleName) }
                        val intersection = annotations.intersect(controllerAnnotations)
                        intersection.forEach { log.debug(it.simpleName) }
                        log.debug("Intersection size: ${intersection.size}")

                        return@memberIter
                    }

                    log.debug("It's return type is {}", it.returnType)

                    val callResult = it.call(kClass.objectInstance)

                    if (callResult == null) {
                        log.warn("Skipping member as we could not get a return type.")
                        displayWrongReturnTypeWarning()
                        return@memberIter
                    }

                    if (callResult !is Function1<*, *>) {
                        log.warn("Skipping member as it's return type is not Function1<*, *> but {}", callResult.javaClass)
                        displayWrongReturnTypeWarning()
                        return@memberIter
                    }

                    val action : M.() -> Any = try {
                        callResult as M.() -> Any
                    } catch (e: Exception) {
                        log.warn("Skipping member as we could not cast the return type to M.() -> Any")
                        displayWrongReturnTypeWarning()
                        log.error("Encountered exception while trying to cast to M.() -> Any", e)
                        return@memberIter
                    }

                    val route = "/" + controllerPath + kClass.simpleName?.decapitalize()

                    log.info("Adding member [{}] to route [{}]", it.name, route)

                    if (isSingular)
                        addSingular(mutant, it, route, action)
                    else
                        addPlural(mutant, it, route, action)
                }

    }

    fun addSingular(mutant: Mutant, it: KCallable<*>, route: String, action: M.() -> Any) {
        when (it.name) {
            "get"       -> mutant.registerRoute(Method.GET,    route, action)
            "create"    -> mutant.registerRoute(Method.POST,   route, action)
            "delete"    -> mutant.registerRoute(Method.DELETE, route, action)
            "update"    -> mutant.registerRoute(Method.PUT,    route, action)
            else        -> addMethod(mutant, it, route + "/" + it.name, action)
        }
    }

    fun addPlural(mutant: Mutant, it: KCallable<*>, route: String, action: M.() -> Any) {
        when (it.name) {
            "index"     -> mutant.registerRoute(Method.GET,    route, action)
            "get"       -> mutant.registerRoute(Method.GET,    route + "/:id", action)
            "create"    -> mutant.registerRoute(Method.POST,   route, action)
            "delete"    -> mutant.registerRoute(Method.DELETE, route + "/:id", action)
            "update"    -> mutant.registerRoute(Method.PUT,    route + "/:id", action)
            else        -> addMethod(mutant, it, route + "/" + it.name, action)
        }
    }

    fun addMethod(mutant: Mutant, it: KCallable<*>, route: String, action: M.() -> Any) {
        if (it.annotations.isEmpty()) {
            log.warn("Skipping member as this member has no annotations.")
            log.warn("You probably want to annotate this member ")
            return
        }

        it.annotations.forEach {
            log.debug("Has annotation {}", it.annotationClass.simpleName)
            log.info("Non resty auto route added! [{}]", route)
            when(it.annotationClass.simpleName) {
                "Get"       -> mutant.registerRoute(Method.GET, route, action)
                "Post"      -> mutant.registerRoute(Method.POST, route, action)
                "Delete"    -> mutant.registerRoute(Method.DELETE, route, action)
                "Put"       -> mutant.registerRoute(Method.PUT, route, action)
                else        -> log.error("Could not add this member, unknown annotation :(")
            }
        }
    }

    fun displayWrongReturnTypeWarning() {
        log.warn("Did you accidentally mark a member with @Get, @Post, @Delete or @Put without")
        log.warn("act { } ? (all actionable members must return M.() -> Any)!")
    }
}
