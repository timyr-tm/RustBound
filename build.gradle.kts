import org.gradle.internal.impldep.kotlinx.serialization.Serializable
import org.slf4j.event.Level

plugins {
    id("java")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.141"
    id("idea")
}

version = findProperty("mod.version")!!.toString()
group = findProperty("mod.group")!!.toString()
base.archivesName = findProperty("mod.id")!!.toString()
java.toolchain.languageVersion = JavaLanguageVersion.of(findProperty("java.version")!!.toString().toInt())

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

neoForge {
    version = findProperty("neoforge.version")!!.toString()

    mods.register(findProperty("mod.id")!!.toString()).get().sourceSet(sourceSets.main.get())

    parchment {
        mappingsVersion = findProperty("parchment.version")!!.toString()
        minecraftVersion = findProperty("minecraft.version")!!.toString()
    }

    runs {
        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", findProperty("mod.id")!!.toString())
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", findProperty("mod.id")!!.toString())
        }

        register("clientData") {
            clientData()

            programArguments.addAll(
                "--all",
                "--mod", findProperty("mod.id")!!.toString(),
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