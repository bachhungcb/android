plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.simplecalculator"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.simplecalculator"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = false
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0") // Thư viện lõi Kotlin
    implementation("androidx.appcompat:appcompat:1.6.1") // Thư viện cho AppCompatActivity
    implementation("com.google.android.material:material:1.10.0") // Thư viện cho Material Design
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Thư viện cho ConstraintLayout

    // ... other dependencies

    // Add the Compose Runtime dependency
    implementation ("androidx.compose.runtime:runtime:1.6.7") // Use a specific, recent version

    // It is also good practice to include other fundamental Compose dependencies


}