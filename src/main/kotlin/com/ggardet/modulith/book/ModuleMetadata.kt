package com.ggardet.modulith.book

import org.springframework.modulith.ApplicationModule
import org.springframework.modulith.NamedInterface
import org.springframework.modulith.PackageInfo

@PackageInfo
@NamedInterface(name = ["book"])
@ApplicationModule(allowedDependencies = ["author :: api"])
class ModuleMetadata
