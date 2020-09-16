plugins {
    id("java-library")
    id("kotlin")
    id("com.jfrog.bintray")
    id("maven-publish")
    id("moxy-publishing-plugin")
}

java {
    withSourcesJar()

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

moxyPublishing {
    artifactName = "moxy-ktx"
    pomName = "Moxy Kotlin Extensions"
    pomDescription = "Kotlin extensions for Moxy"
}
