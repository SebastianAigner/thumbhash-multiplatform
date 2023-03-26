plugins {
    kotlin("multiplatform") version "1.8.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.0-Alpha"
    id("org.jetbrains.compose") version "1.3.1"
}

group = "io.sebi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
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
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation(compose.material3)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

java.sourceSets.getByName("main") {
    java.srcDir("src/jvmMain/kotlin")
}

java.sourceSets.getByName("test") {
    java.srcDir("src/jvmTest/kotlin")
}

//java.sourceSets.main {
//    java.srcDir("src/jvmMain/kotlin")
//}
//
//java.sourceSets.test {
//    java.srcDir("src/jvmTest/kotlin")
//}
