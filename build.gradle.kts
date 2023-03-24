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
//    js(IR) {
//        nodejs()
//        binaries.executable()
//    }
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

java.sourceSets.test {
    java.srcDir("src/jvmTest/kotlin")
}

application {
    mainClass.set("MainKt")
}