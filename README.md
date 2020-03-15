# Using JUnit 4 and JUnit 5 with Kotlin

## JUnit 4 and JUnit 5

Both JUnit 4 and JUnit 5 can be used together within the same project.

```kts
dependencies {
  /* JUnit 4 */
  testImplementation("junit:junit:4.13")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.6.0")

  /* JUnit 5 */
  testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}
```

Gradle needs to be configured to use JUnit Test Platform when running the tests. 

```kts
tasks.test {
  useJUnitPlatform()
}
```

Both the Vintage and the Jupiter test engines will be used by default and both JUnit 4 and JUnit 5 tests will be executed by the respective test engine.

Furthermore, it is possible to use both JUnit 4 and JUnit 5 annotations in the same test class as shown next.

```kotlin
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
```

The full example can be found at: [HelloJUnitMixedTest.kt](src/test/kotlin/com/albertattard/junit/HelloJUnitMixedTest.kt).

JUnit also allows the use of JUnit 4 annotations, such as `@Ignore`, together with the JUnit 5 (the Jupiter test engine).  This requires the following dependency.

```kts
dependencies {
  /* Ue JUnit 4 annotations with JUnit 5*/
  testImplementation("org.junit.jupiter:junit-jupiter-migrationsupport:5.6.0")
}
```

The following example shows how to use the `@Ignore` JUnit 4 annotations with JUnit 5.

```kotlin
@EnableJUnit4MigrationSupport
class MigrateToJUnit5IgnoreOrDisabledTest {

  @Test
  @Ignore
  fun `should not run as it is ignored using JUnit 4 annotations`() {
    fail("should not run as it is ignored using JUnit 4 annotations")
  }

  /* Other methods removed for brevity */
}
```

The full example can be found at: [MigrateToJUnit5IgnoreOrDisabledTest.kt](src/test/kotlin/com/albertattard/junit/MigrateToJUnit5IgnoreOrDisabledTest.kt).

## Lifecycle

In JUnit 4 offered little control over the test class lifecycle.  The test class is created before every test.  JUnit 5 provides two lifecycle methods:

1. `Lifecycle.PER_METHOD` (similar to the JUnit 4 behaviour)
1. `Lifecycle.PER_CLASS` (new lifecycle)

This effect the order in which some lifecycle methods are called.  Let’s assume we have two tests methods.  The following table shows when each method is called.

| `Lifecycle.PER_METHOD` | `Lifecycle.PER_CLASS`  |
| ---------------------- | ---------------------- |
| `@BeforeAll`           | `init{}` (Constructor) |
| `init{}` (Constructor) | `@BeforeAll`           |
| `@BeforeEach`          | `@BeforeEach`          |
| Test 1                 | Test 1                 |
| `@AfterEach`           | `@AfterEach`           |
| `init{}` (Constructor) |                        |
| `@BeforeEach`          | `@BeforeEach`          |
| Test 2                 | Test 2                 |
| `@AfterEach`           | `@AfterEach`           |
| `@AfterAll`            | `@AfterAll`            |

Note that the constructor was called twice when using the `Lifecycle.PER_METHOD` lifecycle and only once when using the `Lifecycle.PER_CLASS`.

### `Lifecycle.PER_METHOD`

In the `Lifecycle.PER_METHOD`, the constructor is called before each test, twice in this case, as there were two tests.  Furthermore, the methods annotated with `@BeforeAll` are called before the constructor is called.  For this to work, methods with the `@BeforeAll` and `@AfterAll` needs to be within a `companion object` and annotated with `@JvmStatic` as shown in the following code fragment.

```kotlin
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class JUnit5TestPerMethodLifecycleTest {

  companion object {
    @BeforeAll
    @JvmStatic
    fun beforeAll() {
      println("@BeforeAll")
    }

    @AfterAll
    @JvmStatic
    fun afterAll() {
      println("@AfterAll")
    }
  }

  init {
    println("init{}")
  }

  /* Other methods removed for brevity */
}
```

While the annotation `@JvmStatic` may seem irrelevant, this add a subtle difference as explain in detail in the [Kotlin reference documentation](https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#static-methods).

The full example can be found at: [JUnit5TestPerMethodLifecycleTest.kt](src/test/kotlin/com/albertattard/junit/JUnit5TestPerMethodLifecycleTest.kt).

### `Lifecycle.PER_CLASS`

In the `Lifecycle.PER_CLASS` the constructor is called once and before anything else.  The methods annotated with the `@BeforeAll` are called after the constructor is called.  Thus, the methods annotated with the `@BeforeAll` and `@AfterAll` do not need to be within a `companion object`.

```kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JUnit5TestPerClassLifecycleTest {

  init {
    println("init{}")
  }

  @BeforeAll
  fun beforeAll() {
    println("@BeforeAll")
  }

  @AfterAll
  fun afterAll() {
    println("@AfterAll")
  }

  /* Other methods removed for brevity */
}
```

The full example can be found at: [JUnit5TestPerClassLifecycleTest.kt](src/test/kotlin/com/albertattard/junit/JUnit5TestPerClassLifecycleTest.kt).

## Mockk

[Mockk](https://mockk.io/) can be used by both JUnit 4 and JUnit 5.  The following fragment shows how this can be setup.

```kotlin
class HelloConsumerTest {

  private val service = mockk<HelloService>("service")
  private val consumer = HelloConsumer(service)

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

  /* Other methods removed for brevity */
}
```

The full examples can be found at: [HelloConsumerJUnt4Test.kt](src/test/kotlin/com/albertattard/junit/HelloConsumerJUnt4Test.kt) and [HelloConsumerJUnt5Test.kt](src/test/kotlin/com/albertattard/junit/HelloConsumerJUnt5Test.kt) respectively.

The mocked `service` and the `consumer`, the class under test, can be access within both test methods.  In JUnit 4, these are created for every test.  When using JUnit 4, two different mocks will be used as shown by the system object identity (obtained by `System.identityHashCode(service)` function).

```bash
Mocked service identity 1062635358
Mocked service identity 1175631958
```

With JUnit 5, and the `Lifecycle.PER_CLASS`, the same mocked instance is reused instead.

```bash
Mocked service identity 1275035040
Mocked service identity 1275035040
```

It is important to clear mocks in JUnit 5 so that each test function receives the mocks in a clear state and not contaminated with residue from previous tests.

```kotlin
    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }
```

This is not required with JUnit 4, as the test class is recreated for every test.

Mockk runs faster with JUnit 5 when compared to JUnit 4, as shown by the following tests examples.

Running just the JUnit 4 version of the test

```bash
$ ./gradlew clean test --tests "*HelloConsumerJUnt4Test"
```

The test produces values close to the following.

| Test                                  | Duration | Result |
| ------------------------------------- | -------: | ------ |
| should greet using the given name     |   1.291s | passed |
| should greet using the default name   |   0.004s | passed |

Similarly, running the JUnit 5 version of the test

```bash
$ ./gradlew clean test --tests "*HelloConsumerJUnt5Test"
```

Produce better results.

| Test                                  | Duration | Result |
| ------------------------------------- | -------: | ------ |
| should greet using the given name()   |   0.633s | passed |
| should greet using the default name() |   0.006s | passed |

Switching the mocks from class level to method level will make JUnit 5 run at the same speed as JUnit 4.

## Support:
1. [https://gitter.im/junit-team/junit5](https://gitter.im/junit-team/junit5)
1. [https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-gradle-kotlin](https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-gradle-kotlin)
1. [https://github.com/junit-team/junit5-samples/tree/master/junit5-migration-gradle](https://github.com/junit-team/junit5-samples/tree/master/junit5-migration-gradle)

## Others:
1. [Spock Framework](http://spockframework.org/)
1. [Spek Framework](https://www.spekframework.org/)
