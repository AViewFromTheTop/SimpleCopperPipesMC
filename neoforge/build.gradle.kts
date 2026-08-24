plugins {
    id("net.frozenblock.triangle.neoforge")
    id("org.quiltmc.gradle.licenser")
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val mod_id: String by project
val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val frozenlib_version: String by project
val cloth_config_version: String by project
val thecopperierage_version: String by project

val neoforge_version: String by project
val neoforge_loader_version_range: String by project

val neoforgeSnapshotMaven = findProperty("neoforge_snapshot_maven") as String?

base {
    archivesName.set(archives_base_name)
}

version = mod_version
group = maven_group

tasks.jar {
    archiveClassifier.set("neoforge")
}

repositories {
    maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
    if (!neoforgeSnapshotMaven.isNullOrBlank()) {
        maven(neoforgeSnapshotMaven) { name = "NeoForge Snapshots" }
    }
    flatDir {
        dirs("libs")
    }
}

neoforge {
    dependOn(project(":scp-common"))
    accessWidener(project(":scp-common"))
}

neoForge {
    accessTransformers {} // Required for transitive AW to apply!
}

tasks {
    processResources {
        val properties = mapOf("mod_version" to version)
        inputs.properties(properties)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(properties)
        }
    }

    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        options.release = 25
        options.isFork = true
        options.isIncremental = true
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
val loaderVariants = setOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements")
configurations.all {
    if (name in loaderVariants) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}

dependencies {
    api("net.frozenblock:frozenlib-neoforge:${frozenlib_version}")?.let {
        accessTransformers(it)
        interfaceInjectionData(it)
    }

    implementation("net.frozenblock:the-copperier-age-neoforge:${thecopperierage_version}")

    implementation("me.shedaniel.cloth:cloth-config-neoforge:${cloth_config_version}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

upload.maven {
    name.set("simplecopperpipes-neoforge")
}
