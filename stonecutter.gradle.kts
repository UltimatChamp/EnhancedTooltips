plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "1.1.0" apply false
    id("dev.kikugie.fletching-table") version "0.1.0-alpha.22" apply false
}

stonecutter parameters {
    var loader = if (node.metadata.project.substringAfterLast('-') == "neo")
                     "neoforge"
                 else node.metadata.project.substringAfterLast('-')
    constants.match(loader, "fabric", "neoforge")
}

stonecutter active "1.21.5-neo"
