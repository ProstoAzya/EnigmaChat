import xyz.jpenilla.resourcefactory.fabric.Environment

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loom)
    alias(libs.plugins.resourcegen)
}

group = "io.github.prostoazya"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.loader)
    implementation(libs.fabric)
    implementation(libs.clothconfig)
    implementation(libs.modmenu)
}

fabricModJson {
    id = project.name.lowercase()
    version = project.version.toString()
    name = "Enigma chat"
    description = ""
    author("ProstoAzya")
    environment = Environment.CLIENT

    depends("fabricloader", ">=${libs.versions.loader.get()}")
    depends("minecraft", "~${libs.versions.minecraft.get()}")
    depends("java", ">=25")
    depends("fabricapi", "*")

    clientEntrypoint("${project.group}.${project.name.lowercase()}.EnigmaChatMod")

    mixin("${id.get()}.mixins.json") {
        this.environment = Environment.CLIENT
    }
}

kotlin {
    jvmToolchain(25)
}

sourceSets.main {
    kotlin.srcDir("src")
    resources.srcDir("resources")
}