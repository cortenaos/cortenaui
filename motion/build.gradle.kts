import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android {
        namespace = "framework.cortena.ui.motion"
        compileSdk = 37
        minSdk = 21
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":foundation"))
            // Motion needs compose.runtime for CompositionLocal + @Stable / @Immutable, and
            // compose.animation.core for SpringSpec / Easing / AnimationSpec primitives.
            // It deliberately does not depend on compose.ui or compose.foundation — motion
            // values are framework primitives, not UI surface.
            implementation(libs.compose.runtime)
            implementation(libs.compose.animation.core)
        }
    }
}
