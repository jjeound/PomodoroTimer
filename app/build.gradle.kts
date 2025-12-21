import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    kotlin("plugin.serialization") version "2.2.20"
}

android {
    namespace = "com.pomodoro.timer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jje.timer"
        minSdk = 26
        targetSdk = 36
        val autoVersionCode = project.findProperty("versionCode")?.toString()?.toInt() ?: 6
        versionCode = autoVersionCode
        versionName = "1.0.${autoVersionCode}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        create("release") {
            storeFile = file("upload-keystore.jks")
            storePassword  = localProperties.getProperty("STORE_PASSWORD") ?: ""
            keyAlias = localProperties.getProperty("KEY_ALIAS") ?: ""
            keyPassword = localProperties.getProperty("KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-receivers")
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
    buildFeatures {
        compose = true
    }
    hilt {
        enableAggregatingTask = true
    }
    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    // Hilt
    implementation (libs.hilt.android)
    ksp (libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    // Paging
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.compose)
    implementation (libs.androidx.paging.common)

    // Room
    implementation (libs.androidx.room.ktx)
    ksp (libs.androidx.room.compiler)
    implementation (libs.androidx.room.paging)
    implementation (libs.androidx.datastore.preferences)

    // Compose
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.compose.colorpicker)
    implementation(libs.lottie)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}