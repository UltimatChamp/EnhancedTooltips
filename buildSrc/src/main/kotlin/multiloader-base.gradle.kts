import dev.kikugie.stonecutter.build.StonecutterBuildExtension

plugins {
    id("java-library")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.kikugie.fletching-table")
    kotlin("jvm")
}

val stonecutter = project.extensions.getByType(StonecutterBuildExtension::class.java)

var isSnapshot = false
var mcVer = project.property("deps.minecraft_version") as String
if (mcVer.contains("-") || mcVer.contains("w")) {
    isSnapshot = true
    mcVer = mcVer.replace("-", "")
}

var loader = stonecutter.current.project.substringAfterLast("-")
if (loader == "neo")
    loader = "neoforge"

version = "${project.property("mod_version")}+$loader.$mcVer"
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("mod_name") as String)
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
    maven("https://maven.parchmentmc.org")
}

project.afterEvaluate {
    stonecutter.let { sc ->
        sc.replacements {
            string {
                direction = sc.eval(sc.current.version, ">1.21.10")
                replace("ResourceLocation", "Identifier")
            }
            string {
                direction = sc.eval(sc.current.version, ">1.21.11")
                replace("GuiGraphics", "GuiGraphicsExtractor")
            }
        }
    }
}

tasks.processResources {
    var awPath = if (stonecutter.eval(stonecutter.current.version, ">1.21.11"))
                     "_noremap"
                 else ""

    val replaceProperties = mapOf(
        "minecraft_range" to project.property("deps.mc_range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.version,
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description"),
        "aw_path" to awPath
    )

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
        expand(replaceProperties)
    }
    replaceProperties.forEach { (key, value) -> inputs.property(key, value) }
}

val javaVer = if (stonecutter.eval(stonecutter.current.version, ">1.21.11"))
                  JavaVersion.VERSION_25
              else JavaVersion.VERSION_21

java {
    withSourcesJar()

    sourceCompatibility = javaVer
    targetCompatibility = javaVer
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
    displayName.set("EnhancedTooltips ${project.version}")

    if (loader == "fabric")
        modLoaders.addAll("fabric", "quilt")
    else modLoaders.add("neoforge")

    modrinth {
        projectId.set(project.property("modrinthId") as String)
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))

        when (mcVer) {
            "26.1.2" -> minecraftVersions.addAll("26.1", "26.1.1", "26.1.2")
            "1.21.11" -> minecraftVersions.add("1.21.11")
            "1.21.10" -> minecraftVersions.addAll("1.21.9", "1.21.10")
            "1.21.8" -> minecraftVersions.addAll("1.21.6", "1.21.7", "1.21.8")
            "1.21.5" -> minecraftVersions.add("1.21.5")
            "1.21.4" -> minecraftVersions.add("1.21.4")
            "1.21.3" -> minecraftVersions.addAll("1.21.2", "1.21.3")
            "1.21.1" -> minecraftVersions.addAll("1.21", "1.21.1")
        }

        requires("yacl")
        if (loader == "fabric")
            requires("fabric-api")

        // Discord
        announcementTitle.set("Download from Modrinth")
    }

    curseforge {
        projectId.set(project.property("curseforgeId") as String)
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))

        when (mcVer) {
            "26.1.2" -> minecraftVersions.addAll("26.1", "26.1.1", "26.1.2")
            "1.21.11" -> minecraftVersions.add("1.21.11")
            "1.21.10" -> minecraftVersions.addAll("1.21.9", "1.21.10")
            "1.21.8" -> minecraftVersions.addAll("1.21.6", "1.21.7", "1.21.8")
            "1.21.5" -> minecraftVersions.add("1.21.5")
            "1.21.4" -> minecraftVersions.add("1.21.4")
            "1.21.3" -> minecraftVersions.addAll("1.21.2", "1.21.3")
            "1.21.1" -> minecraftVersions.addAll("1.21", "1.21.1")
        }

        javaVersions.add(javaVer)

        clientRequired.set(true)
        serverRequired.set(false)

        requires("yacl")
        if (loader == "fabric")
            requires("fabric-api")

        // Discord
        announcementTitle.set("Download from CurseForge")
        projectSlug.set("enhancedtooltips")
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
