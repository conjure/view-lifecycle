import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

val versionName = "1.0.0-alpha02"

android {
    namespace = "uk.co.conjure.view_lifecycle"
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.
                from(components["release"])

                // You can then customize attributes of the publication as shown below.
                groupId = "uk.co.conjure"
                artifactId = "view-lifecycle"
                version = versionName
            }
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        templatesDir = file("dokka/templates")
        footerMessage = "(c) 2022 Conjure Ltd."
        separateInheritedMembers = false
        mergeImplicitExpectActualDeclarations = false
    }
    dokkaSourceSets.configureEach {

        val readmeFile = file("$projectDir/README.md")
        println("Readme file: $readmeFile")
        // If the module has a README, add it to the module's index
        if (readmeFile.exists()) {
            println("Readme file added")
            includes.from(readmeFile)
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.5.1")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.8.10")
    }
}