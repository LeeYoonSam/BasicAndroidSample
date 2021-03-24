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

    const val gradle = "4.1.3"
    const val kotlin = "1.4.31"
    const val appcompat = "1.2.0"
    const val coreKtx = "1.3.2"
    const val material = "1.3.0"
    const val constraintlayout = "2.0.4"
}

object Depends {
    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    }

    const val material = "com.google.android.material:material:${Versions.material}"

    object Lint {
        const val ktlint = "com.pinterest:ktlint:${Versions.Lint.ktlint}"
        const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.Lint.detektFormatting}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"

        object AndroidTest {
            const val androidJunit = "androidx.test.ext:junit:${Versions.Test.androidJunit}"
            const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.Test.espressoCore}"
        }
    }
}