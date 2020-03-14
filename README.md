# Using JUnit 4 and JUnit 5 with Kotlin

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

The full example can be found at: [JUnit5TestPerMethodLifecycleTest.kt](src/test/kotlin/com/albertattard/junit/JUnit5TestPerMethodLifecycleTest.kt)

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

The full example can be found at: [JUnit5TestPerClassLifecycleTest.kt](src/test/kotlin/com/albertattard/junit/JUnit5TestPerClassLifecycleTest.kt)

# Support:
1. [https://gitter.im/junit-team/junit5](https://gitter.im/junit-team/junit5)
1. [https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-gradle-kotlin](https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-gradle-kotlin)
1. [https://github.com/junit-team/junit5-samples/tree/master/junit5-migration-gradle](https://github.com/junit-team/junit5-samples/tree/master/junit5-migration-gradle)

# Others:
1. [Spock Framework](http://spockframework.org/)
1. [Spek Framework](https://www.spekframework.org/)
