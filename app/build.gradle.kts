plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mspp_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mspp_project"
        minSdk = 24
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.1")
    implementation("com.google.firebase:firebase-dynamic-links-ktx:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firebase
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1") // Firebase Authentication with Kotlin extensions

    implementation(platform("com.google.firebase:firebase-bom:32.8.1")) // Firebase BOM
    implementation("com.google.firebase:firebase-auth-ktx") // Firebase Authentication with Kotlin extensions
    implementation("com.sun.mail:android-mail:1.6.5")
    implementation("com.sun.mail:android-activation:1.6.5")

    //Google sign-in providing
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Facebook sign-in providing
    implementation("com.facebook.android:facebook-login:13.0.1")
    implementation("com.facebook.android:facebook-android-sdk:latest.release")

    //Drawer menu
    implementation("com.google.android.material:material:1.11.0")

}