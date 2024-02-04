plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "domain"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.kotlin.coroutines)
            implementation(libs.kotlin.std)
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(libs.koin.kmp)
            implementation(libs.kotlin.serialization)
            implementation(libs.ktor.core)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.negotiation)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.test.turbine)
            implementation(libs.test.kotlin.coroutines)
            implementation(libs.test.runner)
        }
    }
}

android {
    namespace = "dev.thiagosouto.marvelpoc.data.remote"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
