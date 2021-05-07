plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "moxy.sample"
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(mapOf(
                    "disableEmptyStrategyCheck" to "false",
                    "enableEmptyStrategyHelper" to "true",
                    "defaultMoxyStrategy" to "moxy.viewstate.strategy.AddToEndSingleStrategy",
                    "moxyEnableIsolatingProcessing" to "true"
                ))
            }
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "Moxy"
            keyPassword = "MoxyRelease"
            storePassword = "MoxyRelease"
            storeFile = file("DemoReleaseKeystore")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    // we've placed source code in "kotlin folder" but you don't have to do that.
    sourceSets {
        getByName("main") {
            java.setSrcDirs(java.srcDirs + file("src/main/kotlin"))
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjvm-default=all")
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.2.0")

    // AndroidX KTX
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Material Design
    implementation("com.google.android.material:material:1.2.1")

    // HTTP client
    implementation("io.ktor:ktor-client-android:1.4.0")
    implementation("io.ktor:ktor-client-json-jvm:1.4.0")
    implementation("io.ktor:ktor-client-serialization-jvm:1.4.0")

    // Image loader
    implementation("io.coil-kt:coil:1.0.0-rc2")

    implementation("com.google.dagger:hilt-android:2.35.1")
    kapt("com.google.dagger:hilt-android-compiler:2.35.1")

    // java.time and other stuff without third-party libraries
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    // Moxy
    // uncomment to test library from local sources
    implementation(project(":moxy-androidx"))
    implementation(project(":moxy-ktx"))
    kapt(project(":moxy-compiler"))

    // uncomment to test library from maven
//    val moxyVersion = "2.2.1"
//    implementation("com.github.moxy-community:moxy-androidx:$moxyVersion")
//    implementation("com.github.moxy-community:moxy-ktx:$moxyVersion")
//    kapt("com.github.moxy-community:moxy-compiler:$moxyVersion")

    testImplementation("junit:junit:4.13")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
}
