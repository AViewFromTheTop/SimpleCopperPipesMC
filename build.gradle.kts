plugins {
	id("fabric-loom") version("1.4-SNAPSHOT")
	`maven-publish`
	eclipse
	idea
	`java-library`
}

val minecraft_version: String by project
val loader_version: String by project
val mixin_extras_version: String by project

val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_api_version: String by project
val frozenlib_version: String by project
val modmenu_version: String by project
val cloth_config_version: String by project

base {
	archivesName = archives_base_name
}

version = mod_version
group = maven_group

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
	maven("https://api.modrinth.com/maven") {
		name = "Modrinth"

		content {
			includeGroup("maven.modrinth")
		}
	}
	maven("https://maven.terraformersmc.com") {
		content {
			includeGroup("com.terraformersmc")
		}
	}
	maven("https://maven.shedaniel.me")
	maven("https://cursemaven.com") {
		content {
			includeGroup("curse.maven")
		}
	}
	maven("https://maven.flashyreese.me/releases")
	maven("https://maven.flashyreese.me/snapshots")
	maven("https://maven.minecraftforge.net")
	maven("https://maven.parchmentmc.org")
	maven("https://minecraft.guntram.de/maven")

	flatDir {
		dirs("libs")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraft_version")
	mappings(loom.layered {
		officialMojangMappings {
			nameSyntheticMembers = false
		}
	})
	modImplementation("net.fabricmc:fabric-loader:$loader_version")

	modApi("io.github.llamalad7:mixinextras-fabric:$mixin_extras_version")?.let { annotationProcessor(it) }

	modCompileOnly("maven.modrinth:create-fabric:0.5.1-d-build.1161+mc1.20.1")

	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

	modApi("maven.modrinth:frozenlib:$frozenlib_version")
	include("maven.modrinth:frozenlib:$frozenlib_version")

	modApi("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version") {
		exclude(group = "net.fabricmc.fabric-api")
		exclude(group = "com.terraformersmc")
	}

	modImplementation("maven.modrinth:modmenu:$modmenu_version")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand("version" to project.version)
	}
}

val javadocJar = tasks.register("javadocJar", Jar::class) {
	dependsOn(tasks.javadoc)
	archiveClassifier = "javadoc"
	from(tasks.javadoc.get().destinationDir)
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
	dependsOn(tasks.classes)
	archiveClassifier = "sources"
	from(sourceSets.main.get().allSource)
}

tasks.withType(JavaCompile::class) {
	options.encoding = "UTF-8"
	// Minecraft 1.18 (1.18-pre2) upwards uses Java 17.
	options.release = 17
	options.isFork = true
	options.isIncremental = true
}

tasks.withType(Test::class) {
	maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17

	withSourcesJar()
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${base.archivesName}"}
	}
}

artifacts {
	archives(sourcesJar)
	archives(javadocJar)
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
