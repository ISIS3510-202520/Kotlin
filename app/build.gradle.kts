import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.googleServices)
}

hilt {
    enableAggregatingTask = false
}

// Use Properties (no need to prefix with java.util)
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

val openAiKey: String = localProperties.getProperty("OPENAI_API_KEY") ?: ""

android {
    namespace = "com.example.here4u"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.here4u"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "OPENAI_API_KEY", "\"$openAiKey\"")
    }

    packaging {
        resources { excludes += "META-INF/atomicfu.kotlin_module" }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // --- Core / UI base ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)          // 1.8.5

    // --- Activity / Compose host ---
    implementation(libs.androidx.activity)              // 1.9.3 (compatible con AGP 8.6.1)
    implementation(libs.androidx.activity.compose)      // 1.9.3

    // --- Compose ---
    implementation(platform(libs.androidx.compose.bom)) // 2024.10.01
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Lifecycle / ViewModel / LiveData ---
    implementation(libs.androidx.lifecycle.runtime.ktx)   // 2.8.6
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // 2.8.6
    implementation(libs.androidx.lifecycle.livedata.ktx)  // 2.8.6

    // --- Room (KSP) ---
    implementation(libs.androidx.room.runtime) // 2.6.1
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.room.testing)

    // --- Hilt (KSP) ---
    implementation(libs.hilt.android) // 2.51.1
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.fragment) // 1.2.0
    implementation(libs.androidx.hilt.navigation.compose)  // 1.2.0

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- Fix agresivo JavaPoet (además del force de abajo) ---
    implementation("com.squareup:javapoet:1.13.0")
    ksp("com.squareup:javapoet:1.13.0")
    // Recap Line graph
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Open AI API connection dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //------ Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
}

// Fuerza JavaPoet 1.13.0 para TODAS las configuraciones (incluida ksp)
configurations.configureEach {
    resolutionStrategy.force("com.squareup:javapoet:1.13.0")
}
