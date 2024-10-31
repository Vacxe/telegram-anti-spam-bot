import java.net.URI

plugins {
    application
    id("idea")
    kotlin("jvm") version "1.9.22"
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
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}