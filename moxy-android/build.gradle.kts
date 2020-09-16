plugins {
    id("com.android.library")
    id("com.jfrog.bintray")
    id("maven-publish")
    id("moxy-publishing-plugin")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(29)

        consumerProguardFiles("/moxy/src/main/resources/META-INF/proguard/moxy.pro")
    }

    buildFeatures {
        buildConfig = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api(project(":moxy"))

    compileOnly(Deps.android)
    compileOnly(project(":stub-android"))
}

moxyPublishing {
    artifactName = "moxy-android"
    pomName = "Moxy Android"
    pomDescription = "Moxy Android library for Android"
}
