import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("maven-publish")
    id("com.google.devtools.ksp")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    val roomVersion = "2.6.1"
    val navComponentVersion = "2.7.5"
    val glideVersion = "4.16.0"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    ksp("com.github.bumptech.glide:compiler:$glideVersion")

    //navigation-component
    api("androidx.navigation:navigation-fragment-ktx:$navComponentVersion")
    api("androidx.navigation:navigation-ui-ktx:$navComponentVersion")

    //admob
    implementation("com.google.android.gms:play-services-ads:22.6.0")

    //MP Charts
    api("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Room
    api("androidx.room:room-runtime:$roomVersion")
    api("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
}

/*java {
    //withJavadocJar()
    withSourcesJar()
}*/

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.basit.libs"
            artifactId = "workout-library"
            version = "1.1.2"
            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            //from(components["java"])
        }
    }
}