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
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }
}

dependencies {
    implementation("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")

    implementation("blue.endless:jankson:${project.property("deps.jankson_version")}")
    jarJar("blue.endless:jankson:${project.property("deps.jankson_version")}")
    if (stonecutter.eval("${project.property("deps.minecraft_version")}", "<1.21.9"))
        "additionalRuntimeClasspath"("blue.endless:jankson:${project.property("deps.jankson_version")}")

    // Compat
    var vers = if (stonecutter.eval("${project.property("deps.minecraft_version")}", ">1.21.3"))
                   "${project.property("deps.minecraft_version")}"
               else "1.21.1"
    compileOnly(fletchingTable.modrinth("sophisticated-backpacks", vers, "neoforge"))
    compileOnly(fletchingTable.modrinth("sophisticated-core", vers, "neoforge"))

    compileOnly(fletchingTable.modrinth("entity-model-features", "${project.property("deps.minecraft_version")}", "neoforge"))
    compileOnly(fletchingTable.modrinth("entitytexturefeatures", "${project.property("deps.minecraft_version")}", "neoforge"))
}

stonecutter {
    replacements.string {
        direction = eval(current.version, ">1.21.10")
        replace("ResourceLocation", "Identifier")
    }
}

tasks.processResources {
    exclude("fabric.mod.json", "enhancedtooltips.accesswidener")
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}
