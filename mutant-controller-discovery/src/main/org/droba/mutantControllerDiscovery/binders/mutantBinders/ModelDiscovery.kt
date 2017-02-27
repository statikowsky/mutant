package org.droba.mutantControllerDiscovery.binders.mutantBinders

import com.google.common.reflect.ClassPath
import mu.KotlinLogging
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import kotlin.reflect.defaultType

private val log = KotlinLogging.logger {}

fun MutantModelBinder.discoverModels(modelPackage: String = "dtos") {

    log.info { "Discovering models in package: '$modelPackage'" }

    val cp = ClassPath.from(ClassLoader.getSystemClassLoader())
    val controllersPackages = cp.topLevelClasses
            .filter { it.name.contains(modelPackage) }
            .distinctBy { it.packageName }
            .sortedBy { it.packageName.length }

    log.debug { "Found packages: ${controllersPackages.joinToString { it.packageName }}" }

    val controllerPackage = controllersPackages.firstOrNull() ?:
            throw Exception("Could not find models package: '$modelPackage'")

    val refs = Reflections(controllerPackage.packageName, SubTypesScanner(false))
    refs.getSubTypesOf(Object::class.java)
            .forEach {
                log.debug { "Storing model type: ${it.typeName}" }
                this.store(it.kotlin.defaultType, it)
            }
}

