plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.140" apply false
}

stonecutter parameters {
    var loader = if (node.metadata.project.substringAfterLast('-') == "neo")
                     "neoforge"
                 else node.metadata.project.substringAfterLast('-')
    constants.match(loader, "fabric", "neoforge")
}

stonecutter active "26.1-fabric"
