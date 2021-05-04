plugins {
    id("java-library")
    id("kotlin")
    id("com.vanniktech.maven.publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(project(":moxy"))

    compileOnly(Deps.coroutines)

    testImplementation(Deps.junit)
    testImplementation(Deps.coroutines)
    testImplementation(Deps.coroutinesTest)
}
