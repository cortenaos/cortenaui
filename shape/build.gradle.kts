import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        namespace = "framework.cortena.ui.shape"
        compileSdk = 37
        minSdk = 21
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":foundation"))
            // Shape only needs the runtime stability annotations (@Stable, @Immutable)
            // and the core graphics / geometry / unit primitives (Shape, Outline, Path,
            // Size, Density, Dp, LayoutDirection). It deliberately does not depend on
            // compose.foundation — all consumers of these shapes already pull foundation in.
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
        }
    }
}
