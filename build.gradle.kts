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
    implementation(kotlin("stdlib"))

    // Required for StateFlow, Dispatchers, and Coroutine Scopes
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    // Required to safely bridge Coroutine execution back to the JavaFX Application Thread
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.11.0")

    testImplementation(kotlin("test"))
}

kotlin {
    // Aligned to Java 21 LTS for optimal stability with JavaFX 22+
    jvmToolchain(21)
}

javafx {
    version = "22.0.2"
    modules("javafx.controls", "javafx.graphics")
}

application {
    // Corrected package pointer to match the root project directory architecture
    mainClass.set("tabletopfx.Main")
}

tasks.processResources {
    exclude("**/desktop.ini", "**/.DS_Store")
}