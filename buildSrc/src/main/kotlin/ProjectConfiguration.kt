const val targetVersion = "2.1.1"
const val kotlin_version = "1.3.61"

object Deps {
    const val android = "com.google.android:android:1.6_r2"
    const val javapoet = "com.squareup:javapoet:1.11.1"
    const val junit = "junit:junit:4.12"
    const val mockito = "org.mockito:mockito-core:2.27.0"
    const val truth = "androidx.test.ext:truth:1.1.0"
    const val compiletesting = "com.google.testing.compile:compile-testing:0.17"
    val asm = arrayOf("org.ow2.asm:asm:7.1", "org.ow2.asm:asm-util:7.1")
    const val autoservice = "com.google.auto.service:auto-service:1.0-rc5"
    const val autocommon = "com.google.auto:auto-common:0.10"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.1"
    const val gradleIncapHelperAnnotations = "net.ltgt.gradle.incap:incap:0.2"
    const val gradleIncapHelperProcessor = "net.ltgt.gradle.incap:incap-processor:0.2"
}