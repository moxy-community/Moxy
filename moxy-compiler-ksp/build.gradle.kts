plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":moxy"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("org.jetbrains.kotlin:kotlin-symbol-processing-api:1.4-M1-dev-experimental-20200626")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

