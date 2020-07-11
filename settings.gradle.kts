include(":moxy")
include(":moxy-android")
include(":moxy-compiler")
include(":moxy-compiler-ksp")
include(":moxy-app-compat")
include(":moxy-androidx")
include(":moxy-material")
include(":moxy-ktx")

include(":stub-android")
include(":stub-appcompat")
include(":stub-androidx")
include(":stub-material")

include(":sample-app")
include(":sample-app-ksp")

project(":stub-android").projectDir = File("moxy-android/stub-android")
project(":stub-appcompat").projectDir = File("moxy-app-compat/stub-appcompat")
project(":stub-androidx").projectDir = File("moxy-androidx/stub-androidx")
project(":stub-material").projectDir = File("moxy-material/stub-material")

project(":moxy").projectDir = File("moxy")
project(":moxy-androidx").projectDir = File("moxy-androidx")
project(":moxy-app-compat").projectDir = File("moxy-app-compat")
project(":moxy-compiler").projectDir = File("moxy-compiler")
project(":moxy-android").projectDir = File("moxy-android")
project(":moxy-material").projectDir = File("moxy-material")
project(":moxy-compiler-ksp").projectDir = File("moxy-compiler-ksp")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "kotlin-ksp",
                "org.jetbrains.kotlin.kotlin-ksp",
                "org.jetbrains.kotlin.ksp" ->
                useModule("org.jetbrains.kotlin:kotlin-ksp:${requested.version}")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        google()
    }
}
