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
        minSdk = 21
        compileSdk = 37
        namespace = "framework.cortena.ui.foundation"
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            // Intentionally empty — foundation has zero external dependencies.
            // This module must remain framework-agnostic so it can be consumed
            // by Compose, View system, and future ROM integration alike.
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
        artifactId = "ui-foundation",
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
        name.set("CortenaUI Foundation")
        description.set(
            "Pure Kotlin design tokens and framework-agnostic shape geometry for CortenaUI. " +
                "Zero external dependencies — usable from Compose, the Android View system, " +
                "and AOSP / Android.bp builds without modification."
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
