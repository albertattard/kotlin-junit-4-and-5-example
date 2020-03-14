plugins {
    kotlin("jvm").version("1.3.70")
    id("org.jlleitschuh.gradle.ktlint").version("9.2.1")
    id("com.github.ben-manes.versions").version("0.28.0")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

repositories {
    mavenLocal()
    jcenter()
}

configurations {
    all {
        resolutionStrategy {
            force(
                "com.pinterest:ktlint:0.36.0",
                "com.pinterest.ktlint:ktlint-reporter-checkstyle:0.36.0"
            )
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")

    testImplementation("junit:junit:4.13")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.6.0")
}

defaultTasks("clean", "ktlintFormat", "dependencyUpdates", "test")
