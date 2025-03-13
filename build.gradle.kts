import java.net.URI

plugins {
    application
    `java-test-fixtures`
    id("idea")
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}

application {
    mainClass = "io.github.vacxe.tgantispam.MainKt"
    applicationName = "telegram-anti-spam-bot"
}

repositories {
    mavenCentral()
    maven {
        url = URI("https://jitpack.io")
    }
}

val integrationTest: SourceSet = sourceSets.create("integrationTest") {
    kotlin {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        srcDir("src/integrationTest/kotlin")
    }
    resources.srcDir("src/integrationTest/resources")
}

configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

val integrationTestTask = tasks.register<Test>("integrationTest") {
    group = "verification"

    useJUnitPlatform()

    testClassesDirs = integrationTest.output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    shouldRunAfter("test")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.2.0")
    implementation("com.charleskorn.kaml:kaml:0.59.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.influxdb:influxdb-client-kotlin:3.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.9.0")

    // Integration testing dependency
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation("org.testcontainers:testcontainers:1.20.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}