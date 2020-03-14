package com.albertattard.junit

@Suppress("ClassName")
class HelloJUnitMixedTest {

    @org.junit.Test
    fun `should run with JUnit 4`() {
        org.junit.Assert.assertEquals("Hello JUnit 4", Hello.greet("JUnit 4"))
    }

    @org.junit.jupiter.api.Test
    fun `should run with JUnit 5`() {
        org.junit.jupiter.api.Assertions.assertEquals("Hello JUnit 5", Hello.greet("JUnit 5"))
    }
}
