plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tourismclient.cultoura"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tourismclient.cultoura"
        minSdk = 26
        targetSdk = 35
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    //Volley (For HTTP REQUESTS)
    implementation ("com.android.volley:volley:1.2.1")

    //Grid dependency
    implementation ("androidx.gridlayout:gridlayout:1.0.0")

    //Glide Dependencies
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.room.ktx)
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // For JSON conversion

    // OkHttp
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    // AndroidX Lifecycle ViewModel KTX (for viewModelScope)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation (libs.material.v160)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}