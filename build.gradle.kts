plugins {
	id("fabric-loom") version("+")
	id("org.quiltmc.gradle.licenser") version("+")
	id("org.ajoberstar.grgit") version("+")
	id("com.modrinth.minotaur") version("+")
	`maven-publish`
	eclipse
	idea
	`java-library`
	java
}

val minecraft_version: String by project
val quilt_mappings: String by project
val parchment_mappings: String by project
val loader_version: String by project

val mod_version: String by project
val mod_id: String by project
val protocol_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_api_version: String by project
val frozenlib_version: String by project
val modmenu_version: String by project
val cloth_config_version: String by project

val local_frozenlib = findProject(":FrozenLib") != null

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

	mixin {
		defaultRefmapName = "mixins.simple_copper_pipes.refmap.json"
	}

	accessWidenerPath = file("src/main/resources/simple_copper_pipes.accesswidener")
	interfaceInjection {
		// When enabled, injected interfaces from dependencies will be applied.
		enableDependencyInterfaceInjection = false
	}
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

val includeModImplementation by configurations.creating
val includeImplementation by configurations.creating

configurations {
	include {
		extendsFrom(includeImplementation)
		extendsFrom(includeModImplementation)
	}
	implementation {
		extendsFrom(includeImplementation)
	}
	modImplementation {
		extendsFrom(includeModImplementation)
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
	maven("https://maven.parchmentmc.org")
	maven("https://maven.quiltmc.org/repository/release") {
		name = "Quilt"
	}
	maven("https://maven.jamieswhiteshirt.com/libs-release") {
		content {
			includeGroup("com.jamieswhiteshirt")
		}
	}

	flatDir {
		dirs("libs")
	}
	mavenCentral()
}


dependencies {
	minecraft("com.mojang:minecraft:$minecraft_version")
	mappings(loom.layered {
		// please annoy treetrain if this doesnt work
		mappings("org.quiltmc:quilt-mappings:$quilt_mappings:intermediary-v2")
		parchment("org.parchmentmc.data:parchment-$parchment_mappings@zip")
		officialMojangMappings {
			nameSyntheticMembers = false
		}
	})
	modImplementation("net.fabricmc:fabric-loader:$loader_version")

	// Create
	modCompileOnly("maven.modrinth:create-fabric:0.5.1-d-build.1161+mc1.20.1")

	// Fabric API
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

	// FrozenLib
	if (local_frozenlib)
		api(project(":FrozenLib", configuration = "namedElements"))?.let { include(it) }
	else
		modApi("maven.modrinth:frozenlib:$frozenlib_version")?.let { include(it) }

	// Cloth Config
	modApi("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version") {
		exclude(group = "net.fabricmc.fabric-api")
		exclude(group = "com.terraformersmc")
	}
	// ModMenu
	modImplementation("maven.modrinth:modmenu:$modmenu_version")

	"datagenImplementation"(sourceSets.main.get().output)
}

tasks {
	processResources {
		val properties = mapOf(
			"mod_id" to mod_id,
			"version" to version,
			"protocol_version" to protocol_version,
			"minecraft_version" to minecraft_version,

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
				"**/*.mixins.json"
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
		// Minecraft 1.20.5 (24w14a) upwards uses Java 21.
		options.release.set(21)
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

val remapJar: Task by tasks
val sourcesJar: Task by tasks
val javadocJar: Task by tasks

tasks.withType(JavaCompile::class) {
	options.encoding = "UTF-8"
	// Minecraft 1.18 (1.18-pre2) upwards uses Java 17.
	options.release = 21
	options.isFork = true
	options.isIncremental = true
}

tasks.withType(Test::class) {
	maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21

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
