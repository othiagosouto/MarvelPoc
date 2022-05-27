import dev.thiagosouto.Libs

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(Libs.Test.mockWebserver)
    api(Libs.Test.okhttp)
}