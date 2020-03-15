package com.albertattard.junit

import java.io.OutputStream

class HelloConsumer(private val hello: HelloService = HelloService) {

    fun greet(name: String? = null, output: OutputStream) {
        val greeting = hello.greet(name ?: "Stranger")
        output.write(greeting.toByteArray())
    }
}
