plugins {
    id("net.frozenblock.triangle.fabric")
    id("org.quiltmc.gradle.licenser")
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val mod_id: String by project
val mod_version: String by project
val subproject_prefix: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project
val fabric_loader_version: String by project

val fabric_api_version: String by project
val frozenlib_version: String by project

val modmenu_version: String by project
val cloth_config_version: String by project
val thecopperierage_version: String by project

base {
    archivesName = archives_base_name
}

version = getModVersion()
group = maven_group

tasks.jar {
    archiveClassifier.set("fabric")
}

fabric {
    dependOn(project(":$subproject_prefix-common"))
    accessWidener(project(":$subproject_prefix-common"))
    dataGen {
        owner = project(":$subproject_prefix-common")
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

dependencies {
    // Fabric
    implementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")
    
    // FrozenLib
    api("net.frozenblock:frozenlib-fabric:$frozenlib_version")

    // The Copperier Age
    implementation("net.frozenblock:the-copperier-age-fabric:$thecopperierage_version")

    // Mod Menu
    implementation("com.terraformersmc:modmenu:$modmenu_version")

    // Cloth Config
    implementation("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version") {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "com.terraformersmc")
    }
}

val githubActions: Boolean = System.getenv("GITHUB_ACTIONS") == "true"
val licenseChecks: Boolean = githubActions

tasks {
    license {
        if (licenseChecks) {
            rule(rootProject.file("codeformat/HEADER"))

            include("**/*.java")
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

val release = findProperty("releaseType") == "stable"

fun getModVersion(): String {
    var version = "$mod_version-mc$minecraft_version"

    if (!release) {
        version += "-unstable"
    }

    return version
}

val changelogText = run {
    val split = rootProject.file("CHANGELOG.md").readText().split("-----------------")
    check(split.size == 2) { "Malformed changelog" }
    split[1].trim()
}

upload {
    maven {
        name.set("$mod_id-fabric")
    }

    forEach {
        changelog = changelogText
    }

    curseforge {
        dependencies {
            required("fabric-api")
            required("frozenlib")
            optional("modmenu")
            optional("cloth-config")
            optional("the-copperier-age")
        }
    }

    modrinth {
        dependencies {
            required("fabric-api")
            required("frozenlib")
            optional("modmenu")
            optional("cloth-config")
            optional("the-copperier-age")
        }
    }
}