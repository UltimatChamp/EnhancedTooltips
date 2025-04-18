pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6-beta.2"
}

stonecutter {
    create(rootProject) {
        versions("1.21.1", "1.21.3", "1.21.4", "1.21.5")
        vcsVersion = "1.21.5"
    }
}

rootProject.name = "EnhancedTooltips"
