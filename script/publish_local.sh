# Publish to local repository, located in root build/ folder. Published libs will be available in sample app
cd ../
./gradlew moxy:publishReleasePublicationToProjectLocalRepository
./gradlew moxy-compiler:publishReleasePublicationToProjectLocalRepository
./gradlew moxy-app-compat:publishReleasePublicationToProjectLocalRepository
./gradlew moxy-androidx:publishReleasePublicationToProjectLocalRepository
./gradlew moxy-material:publishReleasePublicationToProjectLocalRepository
./gradlew moxy-android:publishReleasePublicationToProjectLocalRepository
./gradlew moxy-ktx:publishReleasePublicationToProjectLocalRepository