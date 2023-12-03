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
    compileSdk = 33

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

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //navigation-component
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    //admob
    implementation("com.google.android.gms:play-services-ads:22.4.0")

    //MP Charts
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
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
            version = "1.0.9"
            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            //from(components["java"])
        }
    }
}