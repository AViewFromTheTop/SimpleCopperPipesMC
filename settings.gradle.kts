pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
        maven("https://files.minecraftforge.net/maven") {
            name = "Forge"
        }
        maven("https://jitpack.io") {
            name = "Jitpack"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Simple Copper Pipes"