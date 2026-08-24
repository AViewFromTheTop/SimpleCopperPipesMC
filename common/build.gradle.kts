plugins {
    id("net.frozenblock.triangle.common")
    id("org.quiltmc.gradle.licenser")
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val frozenlib_version: String by project
val cloth_config_version: String by project

common {
    accessWidener()
}

neoForge {
    accessTransformers {} // Required for transitive AW to apply!
}

dependencies {
    compileOnly("net.frozenblock:frozenlib-common:${frozenlib_version}")?.let {
        accessTransformers(it)
        interfaceInjectionData(it)
    }

    compileOnly("me.shedaniel.cloth:cloth-config:${cloth_config_version}")
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

upload.maven {
    name.set("simplecopperpipes-common")
}
