object Versions {
    object Android {
        const val compileSdk = 30
        const val minSdk = 23
        const val targetSdk = 30
        const val versionCode = 1
        const val versionName = "1.0"
    }
}

object Depends {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.3"

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"

        object Activity {
            private const val version = "1.3.1"
            const val activity = "androidx.activity:activity-ktx:$version"
            const val compose = "androidx.activity:activity-compose:$version"
        }

        const val fragment = "androidx.fragment:fragment-ktx:1.3.6"
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val browser = "androidx.browser:browser:1.3.0"

        object Lifecycle {
            const val lifecycleVersion = "2.4.0"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        }
    }

    object Kotlin {
        const val kotlinVersion = "1.5.21"
        const val coroutineVersion = "1.5.2"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.2.1"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion"

        object Test {
            const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        }
    }

    object Square {
        const val okHttpVersion = "4.9.2"
        const val okhttp3_logging = "com.squareup.okhttp3:logging-interceptor:${okHttpVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"

        object Test {
            const val okhttpMockWebServer = "com.squareup.okhttp3:mockwebserver:${okHttpVersion}"
        }
    }

    object Dagger {
        const val hiltVersion = "2.40.1"
        const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    }

    const val material = "com.google.android.material:material:1.3.0"
    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.3.1"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val inject = "javax.inject:javax.inject:1"

    object Lint {
        const val ktlint = "com.pinterest:ktlint:0.41.0"
        const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:1.16.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val androidJunit = "androidx.test.ext:junit:1.1.2"
        const val androidxCore = "androidx.test:core-ktx:1.4.0"
        const val androidxJunit = "androidx.test.ext:junit-ktx:1.1.3"
        const val androidxRules = "androidx.test:rules:1.2.0"
        const val androidxRunner = "androidx.test:runner:1.4.0"
        const val robolectric = "org.robolectric:robolectric:4.6.1"
        const val threeTenBp = "org.threeten:threetenbp:1.5.2"
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:4.0.0"
    }
}