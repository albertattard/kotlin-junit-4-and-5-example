package com.albertattard.junit

import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelloConsumerJUnt5Test {

    private val service = mockk<HelloService>("service")
    private val consumer = HelloConsumer(service)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(service)
    }

    @Test
    fun `should greet using the given name`() {
        println("Mocked service identity ${System.identityHashCode(service)}")

        val name = "Albert"
        val greeting = "Hello $name"
        every { service.greet(name) } returns greeting

        val message = ByteArrayOutputStream().use { output ->
            consumer.greet(name = name, output = output)
            output.toByteArray().toString(Charsets.UTF_8)
        }

        assertEquals(greeting, message)

        verify(exactly = 1) { service.greet(name) }
    }

    @Test
    fun `should greet using the default name`() {
        println("Mocked service identity ${System.identityHashCode(service)}")

        val name = "Stranger"
        val greeting = "Hello $name"
        every { service.greet(name) } returns greeting

        val out = ByteArrayOutputStream().use { output ->
            consumer.greet(name = null, output = output)
            output.toByteArray().toString(Charsets.UTF_8)
        }

        assertEquals(greeting, out)

        verify(exactly = 1) { service.greet(name) }
    }
}
