import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

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
}

tasks.withType<DokkaTask>().configureEach {
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        templatesDir = file("dokka/templates")
        footerMessage = "(c) 2022 Conjure Ltd."
        separateInheritedMembers = false
        mergeImplicitExpectActualDeclarations = false
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