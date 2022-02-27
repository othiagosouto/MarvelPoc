package dev.thiagosouto


object Versions {
    const val ktlint = "0.43.0"
}

object Libs {

    object Config {
        object Android {
            const val androidCompileSdkVersion = 32
            const val androidBuildToolsVersion = "32.0.0"
            const val minSdkVersion = 21
            const val targetSdkVersion = 32
            const val versionCode = 1
            const val versionName = "1.0"
        }
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.4"

    object Kotlin {
        private const val version = "1.6.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        private const val lifecycleVersion = "2.2.0"
        private const val roomVersion = "2.4.0"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val paging = "androidx.paging:paging-runtime-ktx:3.1.0"
        const val androidxAppCompat = "androidx.appcompat:appcompat:1.4.0"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
        const val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:2.1.2"
        const val material = "com.google.android.material:material:1.4.0"
        const val room = "androidx.room:room-ktx:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"

        object Compose {
            const val version = "1.1.0"

            const val foundation = "androidx.compose.foundation:foundation:${version}"
            const val layout = "androidx.compose.foundation:foundation-layout:${version}"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val runtime = "androidx.compose.runtime:runtime:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val animation = "androidx.compose.animation:animation:${version}"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val activityCompose = "androidx.activity:activity-compose:1.4.0"
            const val coil = "io.coil-kt:coil-compose:1.4.0"


        }

        object Lifecycle {
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0"
        }

        object ConstraintLayout {
            const val constraintLayoutCompose =
                "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
            const val archRules = "androidx.arch.core:core-testing:2.1.0"


            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }
    }

    object Network {
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.8.1"
        const val glide = "com.github.bumptech.glide:glide:4.11.0"
        const val glideCompiler = "com.github.bumptech.glide:compiler:4.11.0"
        const val gson = "com.google.code.gson:gson:2.8.5"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.6.1"
    }

    object Test {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
        const val mockk = "io.mockk:mockk:1.10.0"
        const val mockkAndroid = "io.mockk:mockk-android:1.12.2"
        const val truth = "com.google.truth:truth:1.1.3"
    }

    object DI {
        const val koin = "io.insert-koin:koin-androidx-viewmodel:2.2.2"
    }
}
