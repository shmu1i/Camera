import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore.properties")
val useKeystoreProperties = keystorePropertiesFile.canRead()
val keystoreProperties = Properties()
if (useKeystoreProperties) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

// Load local.properties for custom configuration
val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
if (localPropertiesFile.canRead()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

plugins {
    id("com.android.application")
    kotlin("android")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    if (useKeystoreProperties) {
        signingConfigs {
            create("release") {
                storeFile = rootProject.file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                enableV4Signing = true
            }

            create("play") {
                storeFile = rootProject.file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["uploadKeyAlias"] as String
                keyPassword = keystoreProperties["uploadKeyPassword"] as String
            }
        }
    }

    compileSdk = 36
    buildToolsVersion = "36.0.0"
    ndkVersion = "28.2.13676358"

    namespace = "app.grapheneos.camera"

    defaultConfig {
        applicationId = "com.mediatek.camera"
        minSdk = 29
        targetSdk = 35
        versionCode = 90
        versionName = versionCode.toString()

        // Custom metadata setting key (read from local.properties, defaults to empty)
        val metadataSettingKey = localProperties.getProperty("metadata.setting.key", "")
        buildConfigField("String", "METADATA_SETTING_KEY", "\"${metadataSettingKey}\"")
    }

    buildTypes {
        getByName("release") {
            // Disabled to speed up build process
            // isShrinkResources = true
            // isMinifyEnabled = true
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (useKeystoreProperties) {
                signingConfig = signingConfigs.getByName("release")
            }
            resValue("string", "app_name", "Camera")
        }

        getByName("debug") {
            //applicationIdSuffix = ".dev"
            resValue("string", "app_name", "Camera")
            // isDebuggable = false
        }

        create("play") {
            initWith(getByName("release"))
            applicationIdSuffix = ".play"
            if (useKeystoreProperties) {
                signingConfig = signingConfigs.getByName("play")
            }
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    // Disable lint checks to speed up build process
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    androidResources {
        localeFilters += listOf("en")
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.core:core-ktx:1.17.0")

    val cameraVersion = "1.5.0"
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-video:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")
    implementation("androidx.camera:camera-extensions:$cameraVersion")

    implementation("com.google.zxing:core:3.5.3")
}
