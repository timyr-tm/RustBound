import org.slf4j.event.Level

plugins {
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.141"
    id("idea")
    kotlin("jvm") version "2.3.21"
}

version = properties["mod.version"].toString()
group = properties["mod.group"].toString()
base.archivesName = properties["mod.id"].toString()
java.toolchain.languageVersion = JavaLanguageVersion.of(properties["java.version"].toString().toInt())

tasks.wrapper.configure {
    distributionType = Wrapper.DistributionType.BIN
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    exclude("**/*.bbmodel")
    exclude("src/generated/**/.cache")
}

configurations{
    val localRuntime by creating
    runtimeClasspath {
        extendsFrom(localRuntime)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("thedarkcolour:kotlinforforge-neoforge:${properties["kotlin_for_forge.version"].toString()}")
}

repositories {
    maven {
        name = "Kotlin for Forge"
        setUrl("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

neoForge {
    version = properties["neoforge.version"].toString()

    mods.register(properties["mod.id"].toString()).get().sourceSet(sourceSets.main.get())

    parchment {
        mappingsVersion = properties["parchment.version"].toString()
        minecraftVersion = properties["minecraft.version"].toString()
    }

    runs {
        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", properties["mod.id"].toString())
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", properties["mod.id"]!!.toString())
        }

        register("clientData") {
            clientData()

            programArguments.addAll(
                "--all",
                "--mod", properties["mod.id"]!!.toString(),
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = Level.DEBUG
        }
    }

}

var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    var replaceProperties = listOf(
        "mod.version", "mod.group", "mod.id", "mod.name",
        "mod.license", "mod.issue_tracker", "mod.update_url",
        "mod.display_url", "mod.logo", "mod.credits",
        "mod.authors", "mod.description", "neoforge.version",
        "parchment.version", "minecraft.version",
        "minecraft.version_range", "java.version",
        "kotlin_for_forge.version", "kotlin_for_forge.version_range"
    ).associateWith { properties.getOrDefault(it, "") }

    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

sourceSets.main.get().resources.srcDirs.forEach { i -> println(i) }

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea.module {
    isDownloadJavadoc = true
    isDownloadSources = true
}