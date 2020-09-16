plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    implementation("com.android.tools.build:gradle:4.0.1")
}