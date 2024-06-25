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
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "UMC_6th"
include(":app")
include(":lib")
include(":lib")
include(":lib")
include(":lib")
include(":lib")
include(":myapplication")
include(":myapplication")
include(":myapplication")
