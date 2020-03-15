package com.albertattard.junit

@Suppress("ClassName")
class HelloServiceJUnitMixedTest {

    @org.junit.Test
    fun `should run with JUnit 4`() {
        org.junit.Assert.assertEquals("Hello JUnit 4", HelloService.greet("JUnit 4"))
    }

    @org.junit.jupiter.api.Test
    fun `should run with JUnit 5`() {
        org.junit.jupiter.api.Assertions.assertEquals("Hello JUnit 5", HelloService.greet("JUnit 5"))
    }
}
