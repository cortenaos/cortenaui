import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
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
            api(project(":foundation"))
            // Motion needs compose.runtime for CompositionLocal + @Stable / @Immutable, and
            // compose.animation.core for SpringSpec / Easing / AnimationSpec primitives.
            // It deliberately does not depend on compose.ui or compose.foundation — motion
            // values are framework primitives, not UI surface.
            implementation(libs.compose.runtime)
            implementation(libs.compose.animation.core)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = false)
    if (project.hasProperty("signingInMemoryKey")) {
        signAllPublications()
    }

    coordinates(
        groupId = group.toString(),
        artifactId = "ui-motion",
        version = version.toString(),
    )

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = SourcesJar.Sources(),
            androidVariantsToPublish = listOf("release"),
        )
    )

    pom {
        name.set("CortenaUI Motion")
        description.set(
            "Compose-aware spring presets, duration tiers, and easing curves for CortenaUI. " +
                "Components read motion specs through LocalMotion rather than constructing " +
                "spring(...) or tween(...) calls inline."
        )
        url.set("https://github.com/cortenaui/cortenaui")
        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("cortenaui")
                name.set("The CortenaOS Project")
                url.set("https://github.com/cortenaui")
            }
        }
        scm {
            url.set("https://github.com/cortenaui/cortenaui")
            connection.set("scm:git:git://github.com/cortenaui/cortenaui.git")
            developerConnection.set("scm:git:ssh://git@github.com/cortenaui/cortenaui.git")
        }
    }
}
