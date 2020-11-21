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

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }


    maven {
        url = uri("https://kotlin.bintray.com/kotlinx")
    }
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.exposed:exposed-core:0.28.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.28.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.28.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.28.1")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.ktorm:ktorm-core:3.2.0")
    //implementation("com.okkero.skedule:skedule:1.2.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.4.10")
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
        //include(dependency("com.okkero.skedule:skedule"))
    }

    relocate("de.md5lukas.commons", "de.md5lukas.lib.commons")
}