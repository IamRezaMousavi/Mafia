import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.github.iamrezamousavi.mafia"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.iamrezamousavi.mafia"
        minSdk = 19
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
        setProperty("archivesBaseName", "Mafia-$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    signingConfigs {
        create("ci") {
            storeFile = System.getenv("ANDROID_NIGHTLY_KEYSTORE")?.let { file(it) }
            storePassword = System.getenv("ANDROID_NIGHTLY_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_NIGHTLY_KEYSTORE_ALIAS")
            keyPassword = System.getenv("ANDROID_NIGHTLY_KEYSTORE_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("nightly") {
            initWith(getByName("release"))
            matchingFallbacks += "release"

            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-NIGHTLY"
            manifestPlaceholders["appName"] = "Mafia Nightly"
            signingConfig = signingConfigs.findByName("ci")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

detekt {
    config.setFrom("$rootDir/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = libs.versions.jdk.get()
    reports {
        html.required = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)
    testImplementation(libs.core.testing)

    implementation(libs.gson)

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
}
