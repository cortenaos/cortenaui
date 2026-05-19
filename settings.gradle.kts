pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "cortenaui"

// Foundation: pure Kotlin token layer, zero framework dependency
include(":foundation")

// Shape: Compose shape system, depends on :foundation. Publishable as a standalone AAR
// so non-CortenaUI Compose consumers can use ComponentShape without pulling :compose.
include(":shape")

// Compose: Compose wrappers + theme layer
include(":compose")

// Catalog: Compose showcase app
include(":catalog")
