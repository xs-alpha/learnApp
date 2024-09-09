pluginManagement {
    repositories {
        maven{ url = uri("https://s01.oss.sonatype.org/content/groups/public")}
        maven { url=uri ("https://jitpack.io") }
        maven { url=uri ("https://maven.aliyun.com/repository/releases") }
//        maven { url 'https://maven.aliyun.com/repository/jcenter' }
        maven { url=uri ("https://maven.aliyun.com/repository/google") }
        maven { url=uri ("https://maven.aliyun.com/repository/central") }
        maven { url=uri ("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url=uri ("https://maven.aliyun.com/repository/public") }
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
//        maven { setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots") }
        maven{setUrl( "http://maven.aliyun.com/nexus/content/groups/public/")}
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/spring/")}
        maven{setUrl("https://www.jitpack.io")}
    }
}

rootProject.name = "learnApp"
include(":app")
 