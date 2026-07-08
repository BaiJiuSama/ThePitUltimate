
plugins {
    id("java")
    id("com.palantir.git-version") version "0.12.3"
    kotlin("jvm") version "2.1.20" apply false
    kotlin("plugin.lombok") version "2.1.20" apply false
    id("io.freefair.lombok") version "8.10" apply false
    id("org.jetbrains.dokka") version "1.9.20" apply false
    alias(libs.plugins.shadow) apply false
}
val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion() // 自动调用 git describe
extra["gitVersionString"] = version.toString()
allprojects {
    extra["gitVersionString"] = version.toString()
}
println("ThePitUltimate version: $version")
tasks.named<Jar>("jar") {
    enabled = false
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
repositories {
    mavenCentral()
}
