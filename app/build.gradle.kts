plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.strollingpasta.bingo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.strollingpasta.bingo"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    //implementation(libs.tensorflow.lite.metadata)
    //implementation(libs.tensorflow.lite.support)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // LiteRT dependencies for Google Play services
    implementation("com.google.android.gms:play-services-tflite-java:16.4.0")
    // Optional: include LiteRT Support Library
    implementation("com.google.android.gms:play-services-tflite-support:16.4.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.3")
    // CameraX
    val camerax_version = "1.4.0"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")
    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")
    // FireBase
    implementation (platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-firestore")
    // 구글 로그인
    implementation ("androidx.credentials:credentials:<latest version>")
    implementation ("androidx.credentials:credentials-play-services-auth:<latest version>")
    implementation ("com.google.android.libraries.identity.googleid:googleid:<latest version>")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit 사용
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // gson 변환

}