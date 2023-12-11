plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
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
            baseName = "data-local"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.koin.kmp)
            implementation(libs.sqldelight.coroutines)
            implementation(project(":data"))
            implementation(project(":domain"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        sourceSets.androidMain.dependencies {
            implementation(libs.koin.kmp)
            implementation(libs.sqldelight.android)
        }

        // or iosMain, windowsMain, etc.
        sourceSets.nativeMain.dependencies {
            implementation(libs.sqldelight.ios)
            implementation(libs.koin.kmp)
        }
    }
}

android {
    namespace = "dev.thiagosouto.marvelpoc.data.local"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("MarvelDatabase") {
            packageName.set("dev.thiagosouto.marvelpoc.data.local")
        }
    }
}
