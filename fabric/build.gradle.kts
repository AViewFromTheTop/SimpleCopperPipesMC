plugins {
    id("net.frozenblock.triangle.fabric")
    id("org.quiltmc.gradle.licenser")
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val fabric_loader_version: String by project
val min_fabric_loader_version: String by project

val mod_id: String by project
val mod_version: String by project
val minecraft_version: String by project
val protocol_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_api_version: String by project
val frozenlib_version: String by project

val modmenu_version: String by project
val cloth_config_version: String by project
val thecopperierage_version: String by project

base {
    archivesName = archives_base_name
}

version = mod_version
group = maven_group

tasks.jar {
    archiveClassifier.set("fabric")
}

fabric {
    dependOn(project(":scp-common"))
    accessWidener(project(":scp-common"))
    dataGen {
        owner = project(":scp-common")
        splitSourceSet("datagen")
    }
}

loom {
    enableTransitiveAccessWideners = true
    interfaceInjection {
        enableDependencyInterfaceInjection = true
    }
}

repositories {
    flatDir {
        dirs("libs")
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
val loaderVariants = setOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements", "includeInternal", "modCompileClasspath")
configurations.all {
    if (name in loaderVariants) {
        attributes {
            attribute(loaderAttribute, "fabric")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "fabric")
            }
        }
    }
}

dependencies {
    implementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

    // FrozenLib
    api("net.frozenblock:frozenlib-fabric:${frozenlib_version}")

    // The Copperier Age
    implementation("net.frozenblock:the-copperier-age-fabric:${thecopperierage_version}")

    // Mod Menu
    implementation("com.terraformersmc:modmenu:$modmenu_version")

    // Cloth Config
    implementation("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version") {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "com.terraformersmc")
    }
}

tasks {
    processResources {
        val properties = mapOf(
            "mod_id" to mod_id,
            "version" to version,
            "protocol_version" to protocol_version,
            "minecraft_version" to "~26.2-",

            "fabric_loader_version" to ">=$min_fabric_loader_version",
            "fabric_api_version" to ">=$fabric_api_version",
            "frozenlib_version" to ">=${frozenlib_version.split('-').firstOrNull()}-"
        )

        properties.forEach { (a, b) -> inputs.property(a, b) }

        filesNotMatching(
            listOf(
                "**/*.java",
                "**/sounds.json",
                "**/lang/*.json",
                "**/.cache/*",
                "**/*.accesswidener",
                "**/*.classtweaker",
                "**/*.cfg",
                "**/*.nbt",
                "**/*.png",
                "**/*.ogg",
                "**/*.mixins.json",
                "**/*.zip"
            )
        ) {
            expand(properties)
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

val sourcesJar: Jar by tasks
val javadocJar: Jar by tasks

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

upload.maven {
    name.set("simplecopperpipes-fabric")
}
