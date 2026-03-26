plugins {
    id("multiloader-base")
    id("net.neoforged.moddev")
}

repositories {
    maven("https://maven.neoforged.net/releases")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

neoForge {
    version = project.property("deps.neoforge") as String
    validateAccessTransformers = true

    runs {
        register("client") {
            gameDirectory = file("run/")
            client()
        }
    }

    mods {
        register(project.property("mod_id") as String) {
            sourceSet(sourceSets["main"])
        }
    }

    if (project.hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (project.property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }
}

dependencies {
    api("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")

    implementation("blue.endless:jankson:${project.property("deps.jankson_version")}")
    jarJar("blue.endless:jankson:${project.property("deps.jankson_version")}")
    if (stonecutter.eval("${project.property("deps.minecraft_version")}", "<1.21.9"))
        "additionalRuntimeClasspath"("blue.endless:jankson:${project.property("deps.jankson_version")}")

    // Compat
    if (stonecutter.eval("${project.property("deps.minecraft_version")}", "<26.1")) {
        compileOnly(fletchingTable.modrinth("sophisticated-backpacks", "${project.property("deps.minecraft_version")} + 1.21.11", "neoforge"))
        compileOnly(fletchingTable.modrinth("sophisticated-storage", "${project.property("deps.minecraft_version")} + 1.21.11", "neoforge"))
        compileOnly(fletchingTable.modrinth("sophisticated-core", "${project.property("deps.minecraft_version")} + 1.21.11", "neoforge"))
    }

    compileOnly(fletchingTable.modrinth("entity-model-features", "${project.property("deps.minecraft_version")}", "neoforge"))
    compileOnly(fletchingTable.modrinth("entitytexturefeatures", "${project.property("deps.minecraft_version")}", "neoforge"))
}

tasks.processResources {
    exclude("fabric.mod.json", "enhancedtooltips.classtweaker", "enhancedtooltips_noremap.classtweaker")
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    additionalFiles.from(tasks.sourcesJar.flatMap { it.archiveFile })
}
