package com.albertattard.junit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Suppress("ClassName")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelloServiceJUnit5Test {

    @Nested
    inner class `Showing Use Of Nesting` {
        @Test
        fun `should return hello world when world is given as a parameter`() {
            assertEquals("Hello World", HelloService.greet("World"))
        }
    }
}
