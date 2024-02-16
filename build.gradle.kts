import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.21"
}

group = "com.mosayed"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.8")

    // Voyager
    val voyagerVersion = "1.0.0"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

    // ktor client
    val ktorVersion = "2.3.8"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // File Picker
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            outputBaseDir.set(project.buildDir.resolve("customOutputDir"))
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "crypto"
            packageVersion = "1.0.0"
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}
