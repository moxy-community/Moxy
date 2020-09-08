package moxy.gradle

import com.jfrog.bintray.gradle.BintrayExtension
import groovy.lang.GroovyObject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publication.maven.internal.deployer.BaseMavenInstaller
import org.gradle.api.tasks.Upload
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withGroovyBuilder
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
    var publishToMaven: Boolean? = null
}

class MoxyPublishing : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create<MoxyPublishingPluginExtension>("moxyPublishing")

        project.afterEvaluate {

            val params = project.extensions.getByType<MoxyPublishingPluginExtension>().toParams(project)
            val credentials = params.credentials

            project.version = params.version // Library version
            project.group = params.groupId // Maven Group ID for the artifact

            val baseMavenInstaller = (project.tasks.getByName("install") as Upload).let { install ->
                install.repositories.getByName("mavenInstaller") as BaseMavenInstaller
            }
            baseMavenInstaller.pom.project {
                populateXml(params)
            }

            project.bintray {
                // User and ApiKey stored in local.properties
                user = credentials.bintrayUser
                key = credentials.bintrayApiKey
                setConfigurations("archives") // When uploading configuration files
                // setPublications("publicationName") // When uploading Maven-based publication files

                publish = true // [Default: false] Whether version should be auto published after an upload
                // dryRun = false // [Default: false] Whether to run this as dry-run, without deploying
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
                            sync = params.publishToMaven // [Default: true] Determines whether to sync the version to Maven Central.
                            user = credentials.mavenUser // OSS user token: mandatory
                            password = credentials.mavenToken // OSS user password: mandatory
                            close = "1" // Optional property. By default the staging repository is closed and artifacts are released to Maven Central. You can optionally turn this behaviour off (by puting 0 as value) and release the version manually.
                        }
                    }
                }
            }
        }
    }

    private fun GroovyObject.populateXml(params: Params) {
        withGroovyBuilder {
            setProperty("groupId", params.groupId)
            setProperty("artifactId", params.artifactName)
            setProperty("version", params.version)
            setProperty("name", params.pomName)
            setProperty("modelVersion", "4.0.0")
            setProperty("description", params.pomDescription)
            setProperty("url", "https://github.com/moxy-community/")
            setProperty("inceptionYear", "2019")
            "licenses" {
                "license" {
                    setProperty("name", "MIT")
                    setProperty("url", "https://opensource.org/licenses/MIT")
                    setProperty("distribution", "repo")
                }
            }
            "developers" {
                "developer" {
                    setProperty("name", "Moxy Community")
                    setProperty("email", "moxy-community@yandex.ru")
                    setProperty("organization", "Moxy Community")
                    setProperty("organizationUrl", "https://github.com/moxy-community/")
                }
            }
            "scm" {
                setProperty("connection", "scm:git@github.com:moxy-community/Moxy.git")
                setProperty("developerConnection", "scm:git@github.com:moxy-community/Moxy.git")
                setProperty("url", "https://github.com/moxy-community/Moxy.git")
            }
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
    val publishToMaven: Boolean,
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
        version = version ?: project.readArtifactVersionFromProperties(artifactName),
        publishToMaven = publishToMaven ?: project.readPublishToMavenFromProperties(),
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

private fun Project.readArtifactVersionFromProperties(module: String): String {
    return rootProperties("publish.properties").getOrDefault(module + "_version", "0.1") as String
}

private fun Project.readPublishToMavenFromProperties(): Boolean {
    return (rootProperties("publish.properties").getOrDefault("publish_to_maven", "false") as String).toBoolean()
}
