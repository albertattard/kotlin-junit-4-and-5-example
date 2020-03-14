package com.albertattard.junit

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JUnit5TestPerClassLifecycleTest {

    init {
        println("init{}")
    }

    @BeforeAll
    fun beforeAll() {
        println("@BeforeAll")
    }

    @BeforeEach
    fun beforeEach() {
        println("@BeforeEach")
    }

    @AfterAll
    fun afterAll() {
        println("@AfterAll")
    }

    @AfterEach
    fun afterEach() {
        println("@AfterEach")
    }

    @Test
    fun `should print test 1`() {
        println("Test 1")
    }

    @Test
    fun `should print test 2`() {
        println("Test 2")
    }
}
