plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.9-fabric"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledReleaseMod", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}
