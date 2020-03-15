package com.albertattard.junit

import org.junit.Assert.assertEquals
import org.junit.Test

class HelloServiceJUnit4Test {

    @Test
    fun `should return hello world when world is given as a parameter`() {
        assertEquals("Hello World", HelloService.greet("World"))
    }
}
