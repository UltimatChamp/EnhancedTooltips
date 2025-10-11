plugins {
    id("dev.architectury.loom") version "1.11-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

var loader = project.property("loom.platform")

var isFabric = loader == "fabric"
var isNeo = loader == "neoforge"

var isSnapshot = false
var mcVer: String = project.property("deps.minecraft_version") as String
if (mcVer.contains("-") || mcVer.contains("w")) {
    isSnapshot = true
    mcVer = mcVer.replace("-", "")
}

version = "${project.property("mod_version")}+$loader.$mcVer"
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("mod_name") as String)
}

repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    mavenCentral()
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
    }
    accessWidenerPath.set(rootProject.file("src/main/resources/enhancedtooltips.accesswidener"))
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("deps.minecraft_version")}")
    mappings(loom.layered {
        mappings("net.fabricmc:yarn:${project.property("deps.yarn_mappings")}:v2")
        if (isNeo) {
            mappings("dev.architectury:yarn-mappings-patch-neoforge:${project.property("deps.layered_mappings")}")
        }
    })

    if (isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

        modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fapi_version")}")
        modImplementation("maven.modrinth:modmenu:${project.property("deps.modmenu_version")}")
    } else if (isNeo) {
        "neoForge"("net.neoforged:neoforge:${property("deps.neoforge")}")
    }

    modImplementation("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")

    include("blue.endless:jankson:${project.property("deps.jankson_version")}")
    modImplementation("blue.endless:jankson:${project.property("deps.jankson_version")}")
}

stonecutter {
    const("fabric", isFabric)
    const("neoforge", isNeo)
}

tasks.processResources {
    val replaceProperties = mapOf(
        "minecraft_range" to project.property("deps.mc_range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.version,
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )
    replaceProperties.forEach { (key, value) -> inputs.property(key, value) }

    if (isFabric) {
        filesMatching("fabric.mod.json") {
            expand(replaceProperties)
        }

        exclude("META-INF/neoforge.mods.toml")
    } else if (isNeo) {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(replaceProperties)
        }

        exclude("fabric.mod.json")
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

publishMods {
    val modVersion = project.property("mod_version") as String
    type = when {
        modVersion.contains("alpha") -> ALPHA
        modVersion.contains("beta") || modVersion.contains("rc") || isSnapshot -> BETA
        else -> STABLE
    }

    changelog.set("# ${project.version}\n${rootProject.file("CHANGELOG.md").readText()}")
    file.set(tasks.remapJar.get().archiveFile)
    displayName.set("EnhancedTooltips ${project.version}")

    if (isFabric) {
        modLoaders.addAll("fabric", "quilt")
    } else if (isNeo) {
        modLoaders.add("neoforge")
    }

    val mrOptions = modrinthOptions {
        projectId.set(project.property("modrinthId") as String)
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))

        if (isFabric) requires("fabric-api")
        optional("yacl")

        // Discord
        announcementTitle.set("Download from Modrinth")
    }

    val cfOptions = curseforgeOptions {
        projectId.set(project.property("curseforgeId") as String)
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))

        if (isFabric) requires("fabric-api")
        optional("yacl")

        // Discord
        announcementTitle.set("Download from CurseForge")
        projectSlug.set("enhancedtooltips")
    }

    when (project.property("deps.minecraft_version") as String) {
        "1.21.1" -> {
            modrinth("m1.21.1") {
                from(mrOptions)
                minecraftVersionRange {
                    start = "1.21"
                    end = "1.21.1"
                }
            }
            curseforge("c1.21.1") {
                from(cfOptions)
                minecraftVersionRange {
                    start = "1.21"
                    end = "1.21.1"
                }
            }
        }
        "1.21.3" -> {
            modrinth("m1.21.3") {
                from(mrOptions)
                minecraftVersionRange {
                    start = "1.21.2"
                    end = "1.21.3"
                }
            }
            curseforge("c1.21.3") {
                from(cfOptions)
                minecraftVersionRange {
                    start = "1.21.2"
                    end = "1.21.3"
                }
            }
        }
        "1.21.4" -> {
            modrinth("m1.21.4") {
                from(mrOptions)
                minecraftVersions.add("1.21.4")
            }
            curseforge("c1.21.4") {
                from(cfOptions)
                minecraftVersions.add("1.21.4")
            }
        }
        "1.21.5" -> {
            modrinth("m1.21.5") {
                from(mrOptions)
                minecraftVersions.add("1.21.5")
            }
            curseforge("c1.21.5") {
                from(cfOptions)
                minecraftVersions.add("1.21.5")
            }
        }
        "1.21.8" -> {
            modrinth("m1.21.8") {
                from(mrOptions)
                minecraftVersions.addAll("1.21.8", "1.21.7", "1.21.6")
            }
            curseforge("c1.21.8") {
                from(cfOptions)
                minecraftVersions.addAll("1.21.8", "1.21.7", "1.21.6")
            }
        }
        "1.21.10" -> {
            modrinth("m1.21.10") {
                from(mrOptions)
                minecraftVersions.addAll("1.21.9", "1.21.10")
            }
            curseforge("c1.21.10") {
                from(cfOptions)
                minecraftVersions.addAll("1.21.9", "1.21.10")
            }
        }
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("UltimatChamp/EnhancedTooltips")
        commitish.set("main")

        // Discord
        announcementTitle.set("Download from GitHub")
    }

    discord {
        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
        username.set("EnhancedTooltips Releases")
        avatarUrl.set("https://cdn.modrinth.com/data/8H6RXl2q/images/5eadf722db0fba952eadb08b8645c186c41e1507.png")

        style {
            look.set("MODERN")
            thumbnailUrl.set("https://cdn.modrinth.com/data/8H6RXl2q/images/e211c73de532ffc1d2c1e78e04d96ee2271dfa61.png")
        }
    }
}
