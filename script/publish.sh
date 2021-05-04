# Publish to Maven Central
# Don't forget to setup mavenCentralRepositoryUsername/mavenCentralRepositoryPassword Gradle Property
cd ../
./gradlew publish --no-daemon --no-parallel
./gradlew closeAndReleaseRepository
