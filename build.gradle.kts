buildscript {
    repositories {
        jcenter()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()

        maven("https://dl.bintray.com/xanderblinov/maven")
    }

    project.afterEvaluate {

        if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("java")) {

            tasks.register("updatePublishVersion") {
                group = "bintraing"
                description = "update publishing version"

                doLast {
                    val module = project.name
                    updatePublishVersion(module)
                }
            }
        }
    }
}

subprojects {
    apply(plugin="org.gradle.checkstyle")

    val checkstyle =tasks.create<Checkstyle>("checkstyle") {
        description = "Runs Checkstyle inspection against ICanPlayer sourcesets."
        group = "moxy"
        configFile = rootProject.file("checkstyle.xml")
        ignoreFailures = false
        isShowViolations = true
        classpath = files()
        exclude("**/*.kt")
        setSource("src/main/java")
    }

    // check code style after project evaluation
    afterEvaluate {
        getTasksByName("check", true).first().dependsOn(checkstyle)
    }
}

val clean = tasks.create<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}

fun updatePublishVersion(module: String) {
    val propertyFile = project . rootProject . file ("publish.properties")
    val developSettings = createProperties()
    propertyFile.reader().use { developSettings.load(it) }
    developSettings.setProperty(module + "_version", targetVersion)
    propertyFile.writer().use { developSettings.store(it, null) }
}