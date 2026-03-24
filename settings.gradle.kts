pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.3"
}

stonecutter {
    create(rootProject) {
        fun register(version: String, vararg loaders: String) = loaders
            .forEach {
                if (it == "fabric" && stonecutter.eval(version, ">1.21.11"))
                    version("$version-$it", version).buildscript = "build.fabric_noremap.gradle.kts"
                else version("$version-$it", version).buildscript = "build.$it.gradle.kts"
            }

        register("1.21.1", "fabric", "neo")
        register("1.21.3", "fabric", "neo")
        register("1.21.4", "fabric", "neo")
        register("1.21.5", "fabric", "neo")
        register("1.21.8", "fabric", "neo")
        register("1.21.10", "fabric", "neo")
        register("1.21.11", "fabric", "neo")
        register("26.1", "fabric")

        vcsVersion = "26.1-fabric"
    }
}

rootProject.name = "EnhancedTooltips"
