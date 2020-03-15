package com.albertattard.junit

import org.junit.Ignore
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.migrationsupport.EnableJUnit4MigrationSupport

@Suppress("ClassName")
@EnableJUnit4MigrationSupport
class MigrateToJUnit5IgnoreOrDisabledTest {

    @Test
    @Ignore
    fun `should not run as it is ignored using JUnit 4 annotations`() {
        fail("should not run as it is ignored using JUnit 4 annotations")
    }

    @Test
    @Disabled
    fun `should not run as it is disabled using JUnit 5 annotations`() {
        fail("should not run as it is disabled using JUnit 5 annotations")
    }
}
