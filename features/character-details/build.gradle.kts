import org.jetbrains.compose.ComposeExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.mokoResources)
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
    ).forEach {
        it.binaries.framework {
            baseName = "character-details"
            isStatic = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.kmp.coil.core.compose)
            implementation(libs.kmp.coil.network)
            implementation(libs.ktor.core)
//            implementation(libs.kmp.moko.resources)
            implementation(libs.kmp.moko.resources.compose)
            implementation(project(":data"))
            implementation(project(":design"))
            implementation(project(":domain"))
            implementation(project(":support:presentation"))

            implementation(libs.koin.kmp)

            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.material)
            implementation(compose.material3)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.test.turbine)
            implementation(libs.test.kotlin.coroutines)
            implementation(libs.test.runner)
        }
        androidMain {
            dependsOn(commonMain.get())
        }

        sourceSets.androidMain.dependencies {
            implementation(libs.android.compose.material)
            implementation(libs.android.ktx.viewmodel)
            implementation(libs.android.lifecycle)
        }

        sourceSets.iosMain.dependencies {
            implementation(compose.material3)
        }
    }
}

android {
    namespace = "dev.thiagosouto.marvelpoc.features.character.details"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true }
    composeOptions {
        kotlinCompilerExtensionVersion = extensions.getByType(ComposeExtension::class.java)
            .dependencies.compiler.auto.substringAfterLast(":")
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
