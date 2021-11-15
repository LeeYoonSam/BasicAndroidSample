object Versions {
    object Android {
        const val compileSdk = 30
        const val minSdk = 23
        const val targetSdk = 30
        const val versionCode = 1
        const val versionName = "1.0"
    }

    object Test {
        const val junit = "4.13.2"
        const val androidJunit = "1.1.2"
        const val espressoCore = "3.3.0"
    }

    object Lint {
        const val ktlint = "0.41.0"
        const val detektFormatting = "1.16.0"
    }

    const val gradle = "7.0.3"
    const val kotlin = "1.5.21"
    const val appcompat = "1.2.0"
    const val coreKtx = "1.3.2"
    const val material = "1.3.0"
    const val constraintlayout = "2.0.4"
    const val threeTenAbp = "1.3.1"
    const val threeTenBp = "1.5.2"
    const val coroutine = "1.2.2"
    const val okhttp = "4.9.2"
}

object Depends {
    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    }

    object Lint {
        const val ktlint = "com.pinterest:ktlint:${Versions.Lint.ktlint}"
        const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.Lint.detektFormatting}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"
        const val androidJunit = "androidx.test.ext:junit:${Versions.Test.androidJunit}"
        const val androidxCore = "androidx.test:core-ktx:1.4.0"
        const val androidxJunit = "androidx.test.ext:junit-ktx:1.1.3"
        const val androidxRules = "androidx.test:rules:1.2.0"
        const val androidxRunner = "androidx.test:runner:1.4.0"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.Test.espressoCore}"
        const val robolectric = "org.robolectric:robolectric:4.6.1"
        const val okhttpMockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
        const val threeTenBp = "org.threeten:threetenbp:${Versions.threeTenBp}"
    }

    object Square {
        const val okhttp3_logging = "com.squareup.okhttp3:logging-interceptor:4.9.2"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    const val material = "com.google.android.material:material:${Versions.material}"
    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenAbp}"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val inject = "javax.inject:javax.inject:1"
}