plugins {
    kotlin("jvm") version "2.2.20"
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))        // Kotlin stdlib
    testImplementation(kotlin("test"))      // JUnit + Kotlin test
}

kotlin {
    jvmToolchain(24)                        // leave as-is if you really use JDK 24
}

// JavaFX configuration
javafx {
    version = "22.0.2"
    modules("javafx.controls", "javafx.graphics")
}

// Point this at your JavaFX Application subclass
application {
    mainClass.set("tabletopfx.ui.Main")
}
