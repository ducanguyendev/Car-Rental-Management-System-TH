plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ungdungthuexe"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.ungdungthuexe"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildFeatures {
        dataBinding = true
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Retrofit for networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    
    // Lifecycle and ViewModel
    implementation(libs.livedata)
    implementation(libs.viewmodel)
    implementation(libs.lifecycle.runtime)
    
    // RecyclerView
    implementation(libs.recyclerview)
    
    // Glide for image loading
    implementation(libs.glide)
    
    // CardView
    implementation(libs.cardview)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}