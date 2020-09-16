package moxy.gradle

import com.jfrog.bintray.gradle.BintrayExtension
import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.XmlProvider
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import com.android.build.gradle.LibraryExtension
import org.gradle.api.tasks.TaskProvider
import java.util.*

abstract class MoxyPublishingPluginExtension {

    // Required, you must provide these properties
    lateinit var artifactName: String
    lateinit var pomName: String
    lateinit var pomDescription: String

    // Optional, if these properties are not provided, they will be replaced with defaults
    var groupId: String? = null
    var version: String? = null
    val publishingProperties: Properties? = null
    var isDryRun: Boolean? = null
}

class MoxyPublishing : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create<MoxyPublishingPluginExtension>("moxyPublishing")

        project.afterEvaluate {

            val params = project.extensions.getByType<MoxyPublishingPluginExtension>().toParams(project)
            val credentials = params.credentials

            project.version = params.version // Library version
            project.group = params.groupId // Maven Group ID for the artifact

            project.extensions.getByType<PublishingExtension>().apply {
                repositories {
                    maven {
                        name = "ProjectLocal"
                        url = rootProject.file("build/localMavenPublish").toURI()
                    }
                }
                publications {
                    register<MavenPublication>("release") {
                        from(getSoftwareComponent())

                        groupId = params.groupId
                        artifactId = params.artifactName
                        version = params.version

                        if (hasAndroidPlugin()) {
                            artifact(createAndroidSourcesJarTask())
                        }

                        pom.withXml { populateXml(params) }
                    }
                }
            }

            project.bintray {
                // User and ApiKey stored in local.properties
                user = credentials.bintrayUser
                key = credentials.bintrayApiKey
                setPublications("release") // When uploading Maven-based publication files

                publish = true // [Default: false] Whether version should be auto published after an upload
                dryRun = params.isDryRun // [Default: false] Whether to run this as dry-run, without deploying
                // override = false // [Default: false] Whether to override version artifacts already published

                // Package configuration. The plugin will use the repo and name properties to check if the package already exists. In that case, there's no need to configure the other package properties (like userOrg, desc, etc).
                pkg.apply {
                    repo = "maven"
                    name = params.artifactName
                    setLicenses("MIT")
                    vcsUrl = "https://github.com/moxy-community/Moxy"
                    version.apply {
                        gpg.apply {
                            sign = true // Determines whether to GPG sign the files. The default is false
                            passphrase = credentials.gpgPassphrase // Optional. The passphrase for GPG signing'
                        }
                        mavenCentralSync.apply {
                            sync = true // [Default: true] Determines whether to sync the version to Maven Central.
                            user = credentials.mavenUser // OSS user token: mandatory
                            password = credentials.mavenToken // OSS user password: mandatory
                            close = "1" // Optional property. By default the staging repository is closed and artifacts are released to Maven Central. You can optionally turn this behaviour off (by puting 0 as value) and release the version manually.
                        }
                    }
                }
            }
        }
    }

    private fun XmlProvider.populateXml(params: Params) {
        asNode().apply {
            appendNode("name", params.pomName)
            appendNode("description", params.pomDescription)
            appendNode("url", "https://github.com/moxy-community/")
            appendNode("inceptionYear", "2019")
            appendNode("licenses").apply {
                appendNode("license").apply {
                    appendNode("name", "MIT")
                    appendNode("url", "https://opensource.org/licenses/MIT")
                    appendNode("distribution", "repo")
                }
            }
            appendNode("developers").apply {
                appendNode("developer").apply {
                    appendNode("name", "Moxy Community")
                    appendNode("email", "moxy-community@yandex.ru")
                    appendNode("organization", "Moxy Community")
                    appendNode("organizationUrl", "https://github.com/moxy-community/")
                }
            }
            appendNode("scm").apply {
                appendNode("connection", "scm:git@github.com:moxy-community/Moxy.git")
                appendNode("developerConnection", "scm:git@github.com:moxy-community/Moxy.git")
                appendNode("url", "https://github.com/moxy-community/Moxy.git")
            }

            // Move dependencies node to the end of pom
            (this["dependencies"] as NodeList).map { it as Node }
                .forEach { dependenciesNode ->
                    remove(dependenciesNode)
                    append(dependenciesNode)
                }
        }
    }

    private fun Project.getSoftwareComponent(): SoftwareComponent {
        return if (hasAndroidPlugin()) {
            // Created by android plugin
            components.getByName("release")
        } else {
            // Created by java plugin
            components.getByName("java")
        }
    }

    private fun Project.hasAndroidPlugin() = project.extensions.findByName("android") != null

    private fun Project.createAndroidSourcesJarTask(): TaskProvider<Jar> {
        return tasks.register<Jar>("androidSourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets.getByName("main").java.srcDirs)
        }
    }
}

/**
 * Parameters, that come from extension, or defaults
 */
private class Params(
    val artifactName: String,
    val pomName: String,
    val pomDescription: String,
    val groupId: String,
    val version: String,
    val isDryRun: Boolean,
    val credentials: Credentials
)

/**
 * moxy.gradle.Credentials, required to publish libraries
 */
private class Credentials(
    val bintrayUser: String?,
    val bintrayApiKey: String?,
    val gpgPassphrase: String?,
    val mavenUser: String?,
    val mavenToken: String?
)

private fun MoxyPublishingPluginExtension.toParams(project: Project): Params {
    val properties = publishingProperties ?: project.rootProperties("local.properties")
    return Params(
        artifactName = artifactName,
        pomName = pomName,
        pomDescription = pomDescription,
        groupId = groupId ?: "com.github.moxy-community",
        version = version ?: project.readArtifactVersionFromProperties(),
        isDryRun = isDryRun ?: project.readIsDryRunFromProperties(),
        credentials = Credentials(
            bintrayUser = properties.getProperty("bintrayUser"),
            bintrayApiKey = properties.getProperty("bintrayApiKey"),
            gpgPassphrase = properties.getProperty("gpgPassphrase"),
            mavenUser = properties.getProperty("mavenUser"),
            mavenToken = properties.getProperty("mavenToken")
        )
    )
}

private fun Project.bintray(block: BintrayExtension.() -> Unit) {
    this.extensions.getByType<BintrayExtension>().apply(block)
}

private fun Project.rootProperties(name: String): Properties {
    return Properties().apply { load(rootProject.file(name).inputStream()) }
}

private fun Project.readArtifactVersionFromProperties(): String {
    return rootProperties("publish.properties").getOrDefault("publishVersion", "0.1") as String
}

private fun Project.readIsDryRunFromProperties(): Boolean {
    return (rootProperties("publish.properties").getOrDefault("isDryRun", "true") as String).toBoolean()
}

private val Project.android get() = this.extensions.getByType<LibraryExtension>()
