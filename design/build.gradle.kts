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
            baseName = "design"
            isStatic = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.kmp.coil.core.compose)
            implementation(libs.kmp.coil.network)
            implementation(libs.kmp.moko.resources)
            implementation(libs.kmp.moko.resources.compose)
            implementation(libs.ktor.core)

            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.material)
            implementation(compose.material3)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain {
            dependsOn(commonMain.get())
        }

        sourceSets.androidMain.dependencies {
            implementation(libs.android.compose.material)
        }

        sourceSets.iosMain.dependencies {
            implementation(compose.material3)
        }
    }
}

android {
    namespace = "dev.thiagosouto.marvelpoc.design"
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
multiplatformResources {
    multiplatformResourcesPackage = "dev.thiagosouto.marvelpoc.design"
}
