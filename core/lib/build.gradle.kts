plugins {
    kotlin("jvm") version "1.7.10"
    `java-library`
    kotlin("plugin.serialization") version "1.7.10"
}

group = "me.johngachihi.codestats.core"

java.targetCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}
repositories {
    mavenCentral()
}