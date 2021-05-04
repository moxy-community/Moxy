plugins {
    id("java")
    id("kotlin")
    id("kotlin-kapt")
    id("com.vanniktech.maven.publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":moxy"))

    implementation(Deps.kotlinStdlibForCompiler)

    implementation(Deps.javapoet)

    implementation(Deps.gradleIncapHelperAnnotations)

    compileOnly(Deps.autocommon)
    compileOnly(Deps.autoservice)

    kapt(Deps.gradleIncapHelperProcessor)
    kapt(Deps.autoservice)

    testImplementation(project.project(":moxy").sourceSets.test.get().output)
    testImplementation(Deps.junit)
    testImplementation(Deps.truth)
    testImplementation(Deps.compiletesting)
    testImplementation(Deps.asm)
    testImplementation(Deps.asmUtil)

    //workaround to use test resources (https://stackoverflow.com/q/24870464)
    testRuntime(files(sourceSets.test.get().output.resourcesDir))
}
