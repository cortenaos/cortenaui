plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "app.cortena.ui.catalog"
    compileSdk = 37

    defaultConfig {
        applicationId = "app.cortena.ui.catalog"
        minSdk = 35
        targetSdk = 37
        versionCode = 2
        versionName = "0.2.0-alpha"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures { compose = true }
}

dependencies {
    // :compose transitively brings in :foundation, :shape, and :motion via api(...).
    implementation(project(":compose"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.cortena.icons.phosphor)
}
