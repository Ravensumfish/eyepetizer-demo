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
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }


    rootProject.name = "Eyepetizer"
    include(":app")

include(":core") //基础
include(":core:utils") //工具
include(":core:net") //网络
include(":core:data_store")//数据存储 如SharedPreference
include(":core:ui") // 高复用率的ui组件
include(":core:image_loader") //加载图片的工具
include(":core:router") //路由

    include(":feature") //特性
    include(":feature:home") //首页
    include(":feature:video") //视频详情页
    include(":feature:search") //搜索页
    include(":feature:notify")

    include(":build-logic")

include(":app",":feature",":feature:home")
