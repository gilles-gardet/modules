package com.ggardet.modulith.core

import org.springframework.modulith.ApplicationModule
import org.springframework.modulith.NamedInterface
import org.springframework.modulith.PackageInfo

@PackageInfo
@NamedInterface(name = ["core"])
@ApplicationModule(type = ApplicationModule.Type.OPEN)
class ModuleMetadata
