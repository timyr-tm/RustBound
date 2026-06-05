import org.slf4j.event.Level

plugins {
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.141"
    id("idea")
    kotlin("jvm") version "2.4.0"
}

val modVersion: String by properties
val modGroup: String by properties
val modId: String by properties

val modMappingsVersion: String by properties

val modDependenciesNeoForgeVersion: String by properties
val modDependenciesMinecraftVersion: String by properties
val modDependenciesK4fVersion: String by properties

version = modVersion
group = modGroup
base.archivesName = modId
java.toolchain.languageVersion = JavaLanguageVersion.of(21)

tasks.wrapper.configure {
    distributionType = Wrapper.DistributionType.BIN
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    exclude("**/*.bbmodel")
    exclude("src/generated/**/.cache")
}

configurations {
    val localRuntime by creating
    runtimeClasspath {
        extendsFrom(localRuntime)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("thedarkcolour:kotlinforforge-neoforge:${modDependenciesK4fVersion}")
}

repositories {
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

neoForge {
    version = modDependenciesNeoForgeVersion

    mods.register(modId).get().sourceSet(sourceSets.main.get())

    parchment {
        mappingsVersion = modMappingsVersion
        minecraftVersion = modDependenciesMinecraftVersion
    }

    runs {
        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("clientData") {
            clientData()

            programArguments.addAll(
                "--all",
                "--mod", modId,
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

val metadataProperties: Map<String, String> = properties
    .filterKeys { key -> key.startsWith("mod") }
    .mapValues { (_, value) -> value.toString() }

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    description = "generateModMetadata"

    inputs.properties(metadataProperties)
    expand(metadataProperties)
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