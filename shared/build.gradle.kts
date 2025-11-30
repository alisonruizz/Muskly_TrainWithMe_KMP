import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Coroutines multiplatform
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

            // DateTime multiplatform
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
            //put your multiplatform dependencies here
            implementation("dev.gitlive:firebase-firestore:1.12.0") // ejemplo versión
            implementation("dev.gitlive:firebase-auth:1.12.0") // opcional si necesitas login
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.muskly_trainwithme_kmp"
    compileSdk = 35
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
