plugins {
	id("net.fabricmc.fabric-loom") version("1.17-SNAPSHOT")
	id("org.quiltmc.gradle.licenser") version("+")
	id("org.ajoberstar.grgit") version("+")
	id("com.modrinth.minotaur") version("+")
	`maven-publish`
	eclipse
	idea
	`java-library`
	java
    checkstyle
}

val minecraft_version: String by project
val loader_version: String by project

val mod_version: String by project
val mod_id: String by project
val protocol_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_api_version: String by project
val frozenlib_version: String by project
val thecopperierage_version: String by project
val modmenu_version: String by project
val cloth_config_version: String by project

base {
	archivesName = archives_base_name
}

version = mod_version
group = maven_group

val datagen by sourceSets.registering {
	compileClasspath += sourceSets.main.get().compileClasspath
	runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

sourceSets {
	main {
		resources {
			srcDirs("src/main/generated")
		}
	}
}

loom {
	runtimeOnlyLog4j = true

	accessWidenerPath = file("src/main/resources/simple_copper_pipes.accesswidener")
	interfaceInjection {
		// When enabled, injected interfaces from dependencies will be applied.
		enableDependencyInterfaceInjection = true
	}
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

loom {
	runs {
		register("datagen") {
			client()
			name("Data Generation")
			source(datagen.get())
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
			//vmArg("-Dfabric-api.datagen.strict-validation")
			vmArg("-Dfabric-api.datagen.modid=$mod_id")

			ideConfigGenerated(true)
			runDir = "build/datagen"
		}

		named("client") {
			ideConfigGenerated(true)
		}
		named("server") {
			ideConfigGenerated(true)
		}
	}
}

val includeImplementation by configurations.creating

configurations {
	include {
		extendsFrom(includeImplementation)
	}
	implementation {
		extendsFrom(includeImplementation)
	}
}

repositories {
	maven("https://jitpack.io")
	exclusiveContent {
		forRepository {
			maven("https://api.modrinth.com/maven") {
				name = "Modrinth"
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
	maven("https://maven.terraformersmc.com") {
		content {
			includeGroup("com.terraformersmc")
		}
	}

	maven("https://maven.shedaniel.me/")
	maven("https://maven.minecraftforge.net/")
	maven("https://maven.jamieswhiteshirt.com/libs-release") {
		content {
			includeGroup("com.jamieswhiteshirt")
		}
	}
    maven("https://maven.frozenblock.net/release") {
        name = "FrozenBlock"
    }

	flatDir {
		dirs("libs")
	}
	mavenCentral()
}


dependencies {
	minecraft("com.mojang:minecraft:$minecraft_version")

	implementation("net.fabricmc:fabric-loader:$loader_version")
	implementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

    // FrozenLib
    api("maven.modrinth:frozenlib:$frozenlib_version")

    // The Copperier Age
    implementation("maven.modrinth:the-copperier-age:$thecopperierage_version")

    // ModMenu
    implementation("maven.modrinth:modmenu:${modmenu_version}")

	// Cloth Config
    implementation("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version") {
		exclude(group = "net.fabricmc.fabric-api")
		exclude(group = "com.terraformersmc")
	}

	"datagenImplementation"(sourceSets.main.get().output)
}

tasks {
    processResources {
        val properties = mapOf(
            "mod_id" to mod_id,
            "version" to version,
            "protocol_version" to protocol_version,
            "minecraft_version" to "~26.2-",//minecraft_version,

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

	register("javadocJar", Jar::class) {
		dependsOn(javadoc)
		archiveClassifier.set("javadoc")
		from(javadoc.get().destinationDir)
	}

	register("sourcesJar", Jar::class) {
		dependsOn(classes)
		archiveClassifier.set("sources")
		from(sourceSets.main.get().allSource)
	}

	withType(JavaCompile::class) {
		options.encoding = "UTF-8"
		// Minecraft 26.1 (26.1-snapshot-1) upwards uses Java 25.
		options.release.set(25)
		options.isFork = true
		options.isIncremental = true
	}

	withType(Test::class) {
		maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
	}
}

val applyLicenses: Task by tasks
val test: Task by tasks
val runClient: Task by tasks
val runDatagen: Task by tasks

val jar: Jar by tasks
val sourcesJar: Jar by tasks
val javadocJar: Jar by tasks

tasks.withType(JavaCompile::class) {
	options.encoding = "UTF-8"
	// Minecraft 26.1 (26.1-snapshot-1) upwards uses Java 25.
	options.release = 25
	options.isFork = true
	options.isIncremental = true
}

tasks.withType(Test::class) {
	maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
}

java {
	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25

	withSourcesJar()
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${base.archivesName}"}
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {

	}
}
