plugins {
    kotlin("multiplatform") version "1.8.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.0-Alpha"
    application
}

group = "io.sebi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting
        val jvmTest by getting
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

java.sourceSets.main {
    java.srcDir("src/jvmMain/kotlin")
}

afterEvaluate {
    println(sourceSets.names)
}

application {
    mainClass.set("MainKt")
}