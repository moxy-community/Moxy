# Publish to bintray with options from publish.properties
cd ../
./gradlew moxy:bintrayUpload
./gradlew moxy-compiler:bintrayUpload
./gradlew moxy-app-compat:bintrayUpload
./gradlew moxy-androidx:bintrayUpload
./gradlew moxy-material:bintrayUpload
./gradlew moxy-android:bintrayUpload
./gradlew moxy-ktx:bintrayUpload