pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (  "https://jitpack.io" )// <--
        jcenter() // Warning: this repository is going to shut down soon

    }
}

rootProject.name = "NovaModa"
include(":app")
include(":data")
include(":domain")
