plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":protocol"))
    implementation(project(":monitor:skywalking")) //todo: impl monitor common lib
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-log4j12:1.7.30")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jooq:jooq:3.14.3")

    val vertxVersion = "3.9.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
    testImplementation("junit:junit:4.13.1")
    testImplementation(project(":monitor:skywalking"))
}

tasks {
    test {
        dependsOn(":downloadSkywalking", ":composeUp")
        rootProject.tasks.findByName("composeUp")!!.mustRunAfter("downloadSkywalking")
        finalizedBy(":composeDown")
    }
}
