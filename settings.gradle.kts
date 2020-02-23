include(":moxy")
include(":moxy-android")
include(":moxy-compiler")
include(":moxy-app-compat")
include(":moxy-androidx")
include(":moxy-material")
include(":moxy-ktx")

include(":stub-android", ":stub-appcompat", ":stub-androidx", ":stub-material")

include(":sample-app")

project(":stub-android").projectDir = File("moxy-android/stub-android")
project(":stub-appcompat").projectDir = File("moxy-app-compat/stub-appcompat")
project(":stub-androidx").projectDir = File("moxy-androidx/stub-androidx")
project(":stub-material").projectDir = File("moxy-material/stub-material")

project(":moxy").projectDir = File("moxy")
project(":moxy-androidx").projectDir = File("moxy-androidx")
project(":moxy-app-compat").projectDir = File("moxy-app-compat")
project(":moxy-compiler").projectDir = File("moxy-compiler")
project(":moxy-android").projectDir = File("moxy-android")
project(":moxy-material").projectDir = File("moxy-material")
