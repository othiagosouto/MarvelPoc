package dev.thiagosouto

/**
 * App dependencies
 */
object Libs {

    /**
     * Constants related to configs
     */
    object Config {
        /**
         * Constants related to android properties from the gradle project
         */
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

    /**
     * Dependencies related to kotlin
     */
    object Kotlin {
        private const val version = "1.6.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.20.0"
    }

    /**
     * Dependencies related to coroutines
     */
    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        const val turbine = "app.cash.turbine:turbine:0.7.0"
    }

    /**
     * Dependencies related to androix
     */
    object AndroidX {
        private const val lifecycleVersion = "2.2.0"
        private const val roomVersion = "2.4.0"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val paging = "androidx.paging:paging-runtime-ktx:3.1.1"
        const val androidxAppCompat = "androidx.appcompat:appcompat:1.4.0"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
        const val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:2.1.2"
        const val material = "com.google.android.material:material:1.4.0"
        const val room = "androidx.room:room-ktx:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"

        /**
         * Dependencies related to jetpack compose
         */
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
            const val graphics = "androidx.compose.animation:animation-graphics:$version"


        }

        /**
         * Dependencies related to test
         */
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

    /**
     * Dependencies related to network
     */
    object Network {
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.8.1"
        const val glide = "com.github.bumptech.glide:glide:4.11.0"
        const val glideCompiler = "com.github.bumptech.glide:compiler:4.11.0"
        const val gson = "com.google.code.gson:gson:2.8.5"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.6.1"
    }

    /**
     * Dependencies related to test
     */
    object Test {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
        const val mockk = "io.mockk:mockk:1.10.0"
        const val mockkAndroid = "io.mockk:mockk-android:1.12.2"
        const val truth = "com.google.truth:truth:1.1.3"
        const val mockWebserver = "com.squareup.okhttp3:mockwebserver:4.9.3"
        const val okhttp = "com.squareup.okhttp3:okhttp:4.9.3"
    }

    /**
     * Dependencies related to DI
     */
    object DI {
        const val koin = "io.insert-koin:koin-android:3.2.0"
    }
}
