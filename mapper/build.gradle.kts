plugins {
    id("org.jetbrains.kotlin.jvm")
}

//todo: remove dependency on intellij
repositories {
    maven(url = "https://jitpack.io") { name = "jitpack" }
    maven(url = "https://www.jetbrains.com/intellij-repository/releases") { name = "intellij-releases" }
}

dependencies {
    implementation(project(":protocol"))
    compileOnly(project(":marker")) //todo: only need MarkerUtils
    implementation("com.github.sh5i:git-stein:v0.5.0")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.9.0.202009080501-r")
    implementation("com.google.guava:guava:29.0-jre")

    val intellijVersion = "202.7660.26"
    compileOnly("com.jetbrains.intellij.platform:core:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:core-impl:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:uast:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.platform:util:$intellijVersion") { isTransitive = false }
    compileOnly("com.jetbrains.intellij.java:java-psi:$intellijVersion") { isTransitive = false }

    testImplementation("junit:junit:4.13.1")
}
