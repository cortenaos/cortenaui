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
        namespace = "framework.cortena.ui.shape"
        compileSdk = 37
        minSdk = 21
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":foundation"))
            // Shape only needs the runtime stability annotations (@Stable, @Immutable)
            // and the core graphics / geometry / unit primitives (Shape, Outline, Path,
            // Size, Density, Dp, LayoutDirection). It deliberately does not depend on
            // compose.foundation — all consumers of these shapes already pull foundation in.
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
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
        artifactId = "ui-shape",
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
        name.set("CortenaUI Shape")
        description.set(
            "Compose-aware shape system for CortenaUI. Bridges the framework-agnostic squircle " +
                "math from :foundation to the Compose Shape API. Publishable as a standalone AAR " +
                "for Compose-only consumers and non-Compose system surfaces alike."
        )
        url.set("https://github.com/cortenaos/cortenaui")
        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("cortenaos")
                name.set("The CortenaOS Project")
                url.set("https://github.com/cortenaos")
            }
        }
        scm {
            url.set("https://github.com/cortenaos/cortenaui")
            connection.set("scm:git:git://github.com/cortenaos/cortenaui.git")
            developerConnection.set("scm:git:ssh://git@github.com/cortenaos/cortenaui.git")
        }
    }
}
