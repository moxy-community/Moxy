plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    compileOnly(Deps.android)
    compileOnly(project(":stub-androidx"))
}