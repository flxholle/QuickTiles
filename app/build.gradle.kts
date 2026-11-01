plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "com.flxholle.quicktiles"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.asdoi.quicktiles"
        minSdk = 24
        targetSdk = 36
        versionCode = 26
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // The CI generates this keystore once and caches it between runs, so every QuickTiles
        // build - debug and release - carries the same signature and installs over the previous
        // version without uninstalling it first. It is a throw-away keystore for personal builds,
        // not something to publish to an app store with.
        create("ci") {
            val ciKeystore = rootProject.file(".ci/debug.keystore")
            if (ciKeystore.exists()) {
                storeFile = ciKeystore
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Falls back to the local debug keystore when the CI keystore is not present, so a
            // release build always produces an installable APK.
            signingConfig = if (rootProject.file(".ci/debug.keystore").exists()) {
                signingConfigs.getByName("ci")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.view)
    implementation(libs.searchpreference)

    implementation(libs.androidx.preference.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}