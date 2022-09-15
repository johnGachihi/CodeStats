plugins {
    kotlin("jvm") version "1.7.10"
    `java-library`
}

group = "me.johngachihi.codestats.core"

java.targetCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(kotlin("stdlib"))
}
repositories {
    mavenCentral()
}