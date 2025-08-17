package com.ggardet.modulith.config

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

class TestCleanupExtension : AfterEachCallback {
    override fun afterEach(context: ExtensionContext) {
        val applicationContext = SpringExtension.getApplicationContext(context)
        val jdbcTemplate = applicationContext.getBean(JdbcTemplate::class.java)
        try {
            jdbcTemplate.execute("SET session_replication_role = replica")
            val cleanupQueries = listOf(
                "DELETE FROM books",
                "DELETE FROM authors",
                "DELETE FROM event_publication"
            )
            var totalDeleted = 0
            cleanupQueries.forEach { query ->
                val deleted = jdbcTemplate.update(query)
                totalDeleted += deleted
            }
            jdbcTemplate.execute("SET session_replication_role = DEFAULT")
        } catch (_: Exception) {
            jdbcTemplate.execute("SET session_replication_role = DEFAULT")
        }
    }
}
