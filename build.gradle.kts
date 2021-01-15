import org.jetbrains.kotlin.gradle.tasks.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.20"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "de.md5lukas"
version = "1.4.20"
description = "Kotlin for spigot"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://repo.lunari.studio/repository/maven-public/")
}

dependencies {
    api("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    api("org.jetbrains.exposed:exposed-core:0.28.1")
    api("org.jetbrains.exposed:exposed-dao:0.28.1")
    api("org.jetbrains.exposed:exposed-jdbc:0.28.1")
    api("org.jetbrains.exposed:exposed-java-time:0.28.1")
    api("org.ktorm:ktorm-core:3.2.0")
    api("org.ktorm:ktorm-support-mysql:3.3.0")
    api("org.ktorm:ktorm-support-postgresql:3.2.0")
    api("org.ktorm:ktorm-support-oracle:3.2.0")
    api("org.ktorm:ktorm-support-sqlserver:3.2.0")
    api("org.ktorm:ktorm-support-sqlite:3.2.0")
    api("me.ddevil:skedule:0.1.3")
    api("com.zaxxer:HikariCP:3.4.5")
    testImplementation(kotlin("test-junit5"))
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<ProcessResources> {
    // Force refresh, because gradle does not detect changes in the variables used by expand
    this.outputs.upToDateWhen { false }
    expand("version" to project.version)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")

    dependencies {
        include(dependency("org.jetbrains.kotlin:.*"))
        include(dependency("org.jetbrains.kotlinx:.*"))
        include(dependency("org.jetbrains.exposed:.*"))
        include(dependency("org.ktorm:.*"))
        include(dependency("com.zaxxer:HikariCP"))
        include(dependency("me.ddevil:skedule"))
    }

    relocate("de.md5lukas.commons", "de.md5lukas.kotlin.lib.commons")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}