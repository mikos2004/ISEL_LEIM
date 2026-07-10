plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    //id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0-RC"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0-RC"
}

android {
    namespace = "dam.a50746.disneyjournal"
    compileSdk = 35

    defaultConfig {
        applicationId = "dam.a50746.disneyjournal"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")
    //implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    implementation("com.google.firebase:firebase-firestore:25.1.4")


    // Ktor
    //implementation("io.ktor:ktor-client-android:2.3.5")
    //implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
    //implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    // org.jetbrains.kotlinx:kotlinx-serialization-json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    // Coil for image loading
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    //GMS
    //implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.play.services.auth)

    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0") // Only available on Android/JVM.
    implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-plugins:3.1.1")//

}