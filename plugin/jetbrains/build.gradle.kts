import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij") version "0.6.3"
    id("org.jetbrains.changelog") version "0.6.2"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
val pluginName: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project

val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project

group = pluginGroup
version = pluginVersion

intellij {
    pluginName = "SourceMarker"
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true

    setPlugins("java", "Groovy", "Kotlin")
}
tasks.getByName("buildSearchableOptions").onlyIf { false } //todo: figure out how to remove
tasks.getByName<JavaExec>("runIde") {
    systemProperty("sourcemarker.debug.capture_logs", true)
}

repositories {
    maven(url = "https://jitpack.io") { name = "jitpack" }
}

dependencies {
    implementation(project(":mapper"))
    implementation(project(":marker"))
    implementation(project(":mentor"))
    implementation(project(":monitor:skywalking"))
    implementation(project(":protocol"))
    implementation(project(":portal"))

    val vertxVersion = "3.9.4"
    implementation("com.github.sh5i:git-stein:v0.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.11.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:2.11.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
    implementation("io.dropwizard.metrics:metrics-core:4.1.15")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    implementation("org.jooq:jooq:3.14.3")
}

tasks {
    patchPluginXml {
        version(pluginVersion)
        sinceBuild(pluginSinceBuild)
        untilBuild(pluginUntilBuild)

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription(
            closure {
                File(rootProject.projectDir, "./README.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md file:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").run { markdownToHTML(this) }
            }
        )

        // Get the latest available change notes from the changelog file
        changeNotes(
            closure {
                changelog.getLatest().toHTML()
            }
        )
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        channels(pluginVersion.split('-').getOrElse(1) { "default" }.split('.').first())
    }

    //todo: should be a way to just add implementation() to dependencies
    getByName("processResources") {
        dependsOn(":portal:build")
        doLast {
            copy {
                from(file("$rootDir/portal/build/distributions/portal.js"))
                into(file("$rootDir/plugin/jetbrains/build/resources/main"))
            }
            copy {
                from(file("$rootDir/portal/build/distributions/portal.js.map"))
                into(file("$rootDir/plugin/jetbrains/build/resources/main"))
            }
        }
    }
}
