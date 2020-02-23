import org.gradle.api.Plugin
import org.gradle.api.Project

class MoxyPublishingPluginExtension {
    // You must provide these properties
    String artifactName
    String pomName
    String pomDescription

    // If these properties are not provided, they will be replaced with defaults
    String groupId
    String version
    Properties publishingProperties
    Boolean publishToMaven
}

class MoxyPublishing implements Plugin<Project> {
    void apply(Project project) {

        def extension = project.extensions.create('moxyPublishing', MoxyPublishingPluginExtension)

        project.afterEvaluate {
            if (extension.groupId == null) {
                extension.groupId = "com.github.moxy-community"
            }
            if (extension.publishingProperties == null) {
                Properties properties = new Properties()
                properties.load(project.rootProject.file('local.properties').newDataInputStream())
                extension.publishingProperties = properties
            }
            if (extension.version == null) {
                extension.version = getArtifactVersion(project, extension.artifactName)
            }
            if (extension.publishToMaven == null) {
                extension.publishToMaven = getPublishToMaven(project)
            }

            project.archivesBaseName = extension.artifactName
            project.group = extension.groupId // Maven Group ID for the artifact

            project.install {
                repositories.mavenInstaller {
                    // This generates POM.xml with proper parameters
                    pom.project {
                        groupId extension.groupId
                        artifactId extension.artifactName
                        version extension.version
                        name extension.pomName
                        modelVersion = '4.0.0'
                        description = extension.pomDescription
                        url = 'https://github.com/moxy-community/'
                        inceptionYear '2019'
                        licenses {
                            license {
                                name 'MIT'
                                url 'https://opensource.org/licenses/MIT'
                                distribution 'repo'
                            }
                        }
                        developers {
                            developer {
                                name 'Moxy Community'
                                email 'moxy-community@yandex.ru'
                                organization = 'Moxy Community'
                                organizationUrl 'https://github.com/moxy-community/'
                            }
                        }
                        scm {
                            connection 'scm:git@github.com:moxy-community/Moxy.git'
                            developerConnection 'scm:git@github.com:moxy-community/Moxy.git'
                            url 'https://github.com/moxy-community/Moxy.git'
                        }
                    }
                }
            }

            project.version = extension.version // Library version

            Properties properties = extension.publishingProperties

            project.bintray {
                // User and ApiKey stored in local.properties
                user = properties.getProperty("bintrayUser")
                key = properties.getProperty("bintrayApiKey")

                configurations = ['archives']
                // Package info for Bintray
                pkg {
                    repo = 'maven'
                    name = extension.artifactName
                    licenses = ['MIT']
                    vcsUrl = 'https://github.com/moxy-community/Moxy'
                    publish = true
                    version {
                        gpg {
                            sign = true
                            passphrase = properties.getProperty("gpgPassphrase")
                        }
                        mavenCentralSync {
                            sync = extension.publishToMaven
                            //[Default: true] Determines whether to sync the version to Maven Central.
                            user = properties.getProperty("mavenUser") //OSS user token: mandatory
                            password = properties.getProperty("mavenToken")
                            //OSS user password: mandatory
                            close = '1'
                            //Optional property. By default the staging repository is closed and artifacts are released to Maven Central. You can optionally turn this behaviour off (by puting 0 as value) and release the version manually.
                        }
                    }
                }
            }
        }
    }

    private static String getArtifactVersion(Project project, String module) {
        Properties developSettings = new Properties()
        developSettings.load(project.rootProject.file('publish.properties').newDataInputStream())
        return developSettings.getOrDefault(module + "_version", '0.1')
    }

    private static boolean getPublishToMaven(Project project) {
        Properties developSettings = new Properties()
        developSettings.load(project.rootProject.file('publish.properties').newDataInputStream())
        return developSettings.getOrDefault("publish_to_maven", 'false').toBoolean()
    }
}