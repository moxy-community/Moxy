plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-ksp") version "1.4-M1-dev-experimental-20200626"
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    google()
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        applicationId = "com.example.sample_app_ksp"
        minSdkVersion(26)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":moxy-androidx"))
    implementation(project(":moxy-compiler-ksp"))
    ksp(project(":moxy-compiler-ksp"))

    implementation("androidx.appcompat:appcompat:1.1.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
