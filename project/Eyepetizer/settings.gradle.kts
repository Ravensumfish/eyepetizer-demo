pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Eyepetizer"
include(":app")
include(":core") //基础
include(":core:utils") //工具
include(":core:net") //网络
include(":feature") //特性
include(":feature:home") //首页
include(":feature:video")
include(":build-logic")
include(":core:image_loader")
include(":core:router")
include(":feature:search")
include(":feature:notify")
