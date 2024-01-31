plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
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
            implementation(libs.koin.kmp)
            implementation("co.touchlab:stately-common:2.0.5")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.test.turbine)
            implementation(libs.test.kotlin.coroutines)
        }
    }
}

android {
    namespace = "dev.thiagosouto.marvelpoc.data"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
