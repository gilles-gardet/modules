package com.ggardet.modulith.config

import org.junit.jupiter.api.extension.ExtendWith

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(TestCleanupExtension::class)
annotation class CleanupTestData
