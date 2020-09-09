plugins {
    id("java-library")
    id("com.jfrog.bintray")
    id("maven-publish")
    id("moxy-publishing-plugin")
}

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compileOnly(Deps.android)

    testImplementation(Deps.junit)
    testImplementation(Deps.mockito)
    testImplementation(Deps.truth)
    testImplementation(Deps.compiletesting)
    testImplementation(Deps.javapoet)
    testAnnotationProcessor(project(":moxy-compiler"))
}

moxyPublishing {
    artifactName = "moxy"
    pomName = "Moxy"
    pomDescription = "Moxy library for Android"
}
