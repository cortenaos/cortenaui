import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.maven.publish)
}

kotlin {
    android {
        namespace = "framework.cortena.ui.compose"
        compileSdk = 37
        minSdk = 35
        compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":foundation"))
            api(project(":shape"))
            api(project(":motion"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
        }
        androidMain.dependencies { implementation(libs.androidx.activity.compose) }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = false)
    if (project.hasProperty("signingInMemoryKey")) {
        signAllPublications()
    }

    coordinates(
        groupId = group.toString(),
        artifactId = "ui",
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
        name.set("CortenaUI Compose")
        description.set(
            "Compose component layer for CortenaUI: Button, Slider, Toggle, Text, Icon, " +
                "ScrollView, Theme, and the rest. Transitively pulls :foundation, :shape, and " +
                ":motion via api dependencies."
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
