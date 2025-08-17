package com.ggardet.modulith

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ArchitectureTests {
    private val modules = ApplicationModules.of(ModulithApplication::class.java)

    @Test
    fun `verify module structure`() {
        modules.verify()
    }

    @Test
    fun `generate module documentation`() {
        val options = Documenter.Options.defaults().withOutputFolder("docs")
        Documenter(modules, options)
            .writeModulesAsPlantUml()
            .writeModuleCanvases()
            .writeIndividualModulesAsPlantUml()
    }
}
