import java.net.URI

plugins {
    application
    id("idea")
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}

application {
    mainClass = "io.github.vacxe.tgantispam.MainKt"
    applicationName = "tgantispam"
}

repositories {
    mavenCentral()
    maven {
        url = URI("https://jitpack.io")
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.2.0")
    implementation("com.charleskorn.kaml:kaml:0.59.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.9.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}