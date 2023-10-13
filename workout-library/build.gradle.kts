plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    //id("androidx.navigation.safeargs.kotlin")
    id("maven-publish")
}

android {
    namespace = "com.basit.workout_library"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //navigation
    //implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    //implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    //Kotlin + coroutines
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //implementation("androidx.work:work-runtime-ktx:2.8.1")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //admob
    implementation("com.google.android.gms:play-services-ads:22.4.0")
}