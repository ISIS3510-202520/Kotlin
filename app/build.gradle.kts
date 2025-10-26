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

// Cargar el archivo .env
val envFile = rootProject.file(".env")
val env = Properties()
if (envFile.exists()) {
    println("Cargando variables desde .env")
    env.load(envFile.inputStream())
} else {
    println("No se encontró el archivo .env en ${envFile.absolutePath}")
}

// local.properties (para claves locales como OPENAI_API_KEY)
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

// Lee llaves/usuarios
val openAiKey: String = localProperties.getProperty("OPENAI_API_KEY") ?: ""
val gmailUser: String = env.getProperty("GMAIL_USERNAME") ?: ""
val gmailAppPassword: String = env.getProperty("GMAIL_APP_PASSWORD") ?: ""

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

        // TODOS los buildConfigField deben estar dentro de defaultConfig o buildTypes
        buildConfigField("String", "OPENAI_API_KEY", "\"$openAiKey\"")
        buildConfigField("String", "EMAIL_USERNAME", "\"$gmailUser\"")
        buildConfigField("String", "EMAIL_APP_PASSWORD", "\"$gmailAppPassword\"")
    }

    packaging {
        resources {
            excludes += "META-INF/atomicfu.kotlin_module"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "/META-INF/{AL2.0,LGPL2.1,NOTICE.md,LICENSE.md,LICENSE.txt,NOTICE.txt,NOTICE,LICENSE}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
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
}

// Dependencias
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.common.jvm)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)



    // OpenAI API / Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coroutines + Play Services
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}
