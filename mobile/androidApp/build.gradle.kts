plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "me.johngachihi.codestats.mobile.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "me.johngachihi.codestats.mobile.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.ui:ui-tooling:1.3.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

    val koinVersion = "3.3.0"
    implementation("io.insert-koin:koin-core:$koinVersion")
    val koinAndroidVersion = "3.3.1"
    implementation("io.insert-koin:koin-android:$koinAndroidVersion")

    testImplementation("junit:junit:4.13.2")
}