rootProject.name = "mydanceKMPsharedUI"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

include(":features:auth")
include(":features:user")

include(":features:student:reservation")
include(":features:student:calendar")

include(":features:academy:students")
include(":features:academy:teachers")
include(":features:academy:classes")

include(":data:auth")
include(":data:user")
include(":data:classes")
include(":data:reservation")
include(":data:students")
include(":data:teachers")

include(":core:ui")
include(":core:firebase")
