@file:Suppress("UnstableApiUsage")

plugins {
    id("multiloader-base")
    id("net.fabricmc.fabric-loom-remap")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("deps.minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        if (project.hasProperty("deps.parchment"))
            parchment("org.parchmentmc.data:parchment-${project.property("deps.parchment")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fapi_version")}")

    implementation("blue.endless:jankson:${project.property("deps.jankson_version")}")
    include("blue.endless:jankson:${project.property("deps.jankson_version")}")

    modImplementation(fletchingTable.modrinth("modmenu", "${project.property("deps.minecraft_version")}", "fabric"))
    modApi("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")

    // Compat
    modCompileOnly(fletchingTable.modrinth("sophisticated-core-(unofficial-fabric-port)", "${project.property("deps.minecraft_version")} + 1.21.1", "fabric"))

    modCompileOnly(fletchingTable.modrinth("entity-model-features", "${project.property("deps.minecraft_version")}", "fabric"))
    modCompileOnly(fletchingTable.modrinth("entitytexturefeatures", "${project.property("deps.minecraft_version")}", "fabric"))
}

loom {
    accessWidenerPath.set(rootProject.file("src/main/resources/enhancedtooltips.classtweaker"))
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks.processResources {
    exclude("META-INF/neoforge.mods.toml", "META-INF/accesstransformer.cfg", "enhancedtooltips_noremap.classtweaker")
}

publishMods {
    file.set(tasks.remapJar.flatMap { it.archiveFile })
    additionalFiles.from(tasks.remapSourcesJar.flatMap { it.archiveFile })
}
