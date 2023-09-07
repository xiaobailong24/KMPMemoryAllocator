plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-module-name=KMP",
                "-Xruntime-logs=gc=info",
                // https://kotlinlang.org/docs/native-memory-manager.html#adjust-memory-consumption
                // Kotlin 1.9.0: mimalloc is default, custom is beta
//                "-Xallocator=custom",
            )
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.kmp.memory.allocator"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}