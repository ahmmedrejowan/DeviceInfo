plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.rejowan.deviceinfo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rejowan.deviceinfo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    implementation(project(mapOf("path" to ":icons")))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-base:18.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // lottie animation
    implementation("com.airbnb.android:lottie:6.2.0")

    // koin for dependency injection
    implementation("io.insert-koin:koin-android:3.5.0")

    // shared preferences
    implementation("androidx.preference:preference-ktx:1.2.1")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")

    // camera2
    implementation("androidx.camera:camera-camera2:1.3.1")

    // MPAndroidChart
    implementation("com.github.r3jo1:MPAndroidChart-Reworked:1.0")

    // Android battery view
    implementation("com.github.ahmmedrejowan:AndroidBatteryView:0.1")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Licensy
    implementation("com.github.ahmmedrejowan:Licensy:0.2")


}