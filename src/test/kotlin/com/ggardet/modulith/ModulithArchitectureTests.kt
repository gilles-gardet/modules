package com.ggardet.modulith

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ModulithArchitectureTests {

    private val modules = ApplicationModules.of(ModulithApplication::class.java)

    @Test
    fun `verify module structure`() {
        modules.verify()
    }

    @Test
    fun `generate module documentation`() {
        Documenter(modules)
            .writeModulesAsPlantUml()
            .writeModuleCanvases()
            .writeIndividualModulesAsPlantUml()
    }

    @Test
    fun `print module structure`() {
        modules.forEach { module ->
            println("Module: ${module.displayName}")
            println("  Base Package: ${module.basePackage}")
            println("  Spring Components: ${module.springBeans.size}")
            println()
        }
    }
}
