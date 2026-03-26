plugins {
    id("multiloader-base")
    id("net.fabricmc.fabric-loom")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("deps.minecraft_version")}")

    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fapi_version")}")

    implementation("blue.endless:jankson:${project.property("deps.jankson_version")}")
    include("blue.endless:jankson:${project.property("deps.jankson_version")}")

    implementation(fletchingTable.modrinth("modmenu", "${project.property("deps.minecraft_version")} + 26.1-pre-2", "fabric"))
    api("dev.isxander:yet-another-config-lib:${project.property("deps.yacl_version")}")

    // Compat
    /*compileOnly(fletchingTable.modrinth("sophisticated-backpacks-(unoffical-fabric-port)", "1.21.1", "fabric")) // intentional spelling mistake
    modCompileOnly(fletchingTable.modrinth("sophisticated-storage-(unofficial-fabric-port)", "1.21.1", "fabric"))
    compileOnly(fletchingTable.modrinth("sophisticated-core-(unofficial-fabric-port)", "1.21.1", "fabric"))*/

    compileOnly(fletchingTable.modrinth("entity-model-features", "${project.property("deps.minecraft_version")}", "fabric"))
    compileOnly(fletchingTable.modrinth("entitytexturefeatures", "${project.property("deps.minecraft_version")}", "fabric"))
}

loom {
    accessWidenerPath.set(rootProject.file("src/main/resources/enhancedtooltips_noremap.classtweaker"))
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks.processResources {
    exclude("META-INF/neoforge.mods.toml", "META-INF/accesstransformer.cfg", "enhancedtooltips.classtweaker")
}

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    additionalFiles.from(tasks.sourcesJar.flatMap { it.archiveFile })
}
