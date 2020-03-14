package com.albertattard.junit

import org.junit.Assert.assertEquals
import org.junit.Test

class HelloJUnit4Test {

    @Test
    fun `should return hello world when world is given as a parameter`() {
        assertEquals("Hello World", Hello.greet("World"))
    }
}
