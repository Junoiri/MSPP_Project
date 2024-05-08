plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mspp_project"
    compileSdk = 34

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }

    defaultConfig {
        applicationId = "com.example.mspp_project"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    configurations {
        all {
            exclude(group = "com.google.protobuf", module = "protobuf-java")
        }
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
    implementation("mysql:mysql-connector-java:8.0.33")
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

    //db + coroutines
    implementation("mysql:mysql-connector-java:5.1.26")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1") // Firebase Authentication with Kotlin extensions

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

    //Number picker
    implementation("io.github.ShawnLin013:number-picker:2.4.13")

    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)

    // Material Design 2
    implementation("androidx.compose.material:material")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.7")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library
    implementation("androidx.compose.material:material-icons-core")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.9.0")

    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")



}