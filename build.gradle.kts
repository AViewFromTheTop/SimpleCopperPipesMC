plugins {
    id("net.frozenblock.triangle.core") version("+")
    id("net.frozenblock.triangle.common") version("+") apply(false)
    id("net.frozenblock.triangle.fabric") version("+") apply(false)
    id("net.frozenblock.candlelight") version("+") apply(false)

    id("org.quiltmc.gradle.licenser") version("+") apply(false)
    checkstyle
}

checkstyle {
    configFile = rootProject.file("checkstyle.xml")
    toolVersion = "10.20.2"
}

val frozenlib_version: String by project

mod {
    additional.add("frozenlib_version", ">=${frozenlib_version.split('-').firstOrNull()}-")
    additional.add("protocol_version")
    additional.add("mod_description")
    additional.add("mod_credits")
    additional.add("mod_license")
    additional.add("mod_homepage")
    additional.add("mod_authors")
    additional.add("mod_github")
}

subprojects {
    apply(plugin = "net.frozenblock.triangle.core")
    apply(plugin = "net.frozenblock.candlelight")

    val mavenUrl = env["MAVEN_URL"]
    val mavenUsername = env["MAVEN_USERNAME"]
    val mavenPassword = env["MAVEN_PASSWORD"]

    if (mavenUrl != null && mavenUsername != null && mavenPassword != null) {
        upload {
            maven {
                repositories {
                    maven(mavenUrl) {
                        name = "FrozenBlock"
                        credentials {
                            username = mavenUsername
                            password = mavenPassword
                        }
                    }
                }
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "4000"))
        options.release.set(25)
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    dependencies {
        compileOnly("net.frozenblock:candlelight:+")
        compileOnly("net.frozenblock:frozenlib-common:${frozenlib_version}")
    }

    repositories {
        maven("https://maven.frozenblock.net/release") {
            name = "FrozenBlock"
        }
        maven("https://maven.frozenblock.net/snapshot") {
            name = "FrozenBlock Snapshot"
        }
        maven("https://maven.terraformersmc.com") {
            name = "TerraformersMC"
            content {
                includeGroup("com.terraformersmc")
            }
        }
        maven("https://registry.somethingcatchy.net/repository/maven-releases/") { // Candlelight & Triangle
            name = "SomethingCatchy (MehVahdJukaar)"
        }
        maven("https://maven.quiltmc.org/repository/release") {
            name = "Quilt"
        }
        maven("https://maven.shedaniel.me/") {
            name = "Shedaniel"
        }
        maven("https://maven.jamieswhiteshirt.com/libs-release") {
            name = "JamiesWhiteShirt"
            content {
                includeGroup("com.jamieswhiteshirt")
            }
        }

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
        maven("https://jitpack.io") {
            name = "Jitpack"
        }
        mavenCentral()
    }

    tasks {
        withType(JavaCompile::class) {
            options.encoding = "UTF-8"
            options.release.set(25)
            options.isFork = true
            options.isIncremental = true
        }

        withType(Test::class) {
            maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
        }
    }
}
