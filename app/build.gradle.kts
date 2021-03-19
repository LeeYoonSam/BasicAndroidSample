plugins {
    id("com.android.application")
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.Android.compileSdk)

    defaultConfig {
        applicationId = "com.ys.basicandroid"
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.targetSdk)
        versionCode(Versions.Android.versionCode)
        versionName(Versions.Android.versionName)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lintOptions {
        disable(
            "UnsafeExperimentalUsageError",
            "UnsafeExperimentalUsageWarning"
        )
    }
}

val ktlint by configurations.creating

tasks.register<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("--android", "src/**/*.kt")
}

tasks.named("check") {
    dependsOn(ktlint)
}

/** 스타일 수정 후 자동으로 수정해 줌 */
tasks.register<JavaExec>("ktlintFormat"){
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("-F", "src/**/*.kt")
}

/** GitHooks 복사 */
tasks.register<Copy>("copyGitHooks"){
    from("${rootDir}/codeConfig/git/git-hooks/") {
        include("**/*")
        rename("(.*)", "$1")
    }

    into("${rootDir}/.git/hooks")
}

/** GitHooks 설치 */
tasks.register<Exec>("installGitHooks"){
    group = "git hooks"
    workingDir(rootDir)
    commandLine("chmod")
    args("-R", "+x", ".git/hooks/")
    dependsOn("copyGitHooks")
}

detekt {
    toolVersion = "1.16.0"
    config = files("$rootDir/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.kotlin)
    implementation(Libs.appcompat)
    implementation(Libs.coreKtx)
    implementation(Libs.material)
    implementation(Libs.constraintlayout)

    testImplementation(TestLibs.junit)

    androidTestImplementation(TestLibs.AndroidTest.androidJunit)
    androidTestImplementation(TestLibs.AndroidTest.espressoCore)

    ktlint(LintLibs.ktlint)
    detektPlugins(LintLibs.detektFormatting)
}