import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 30
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    //DI
    implementation ("io.insert-koin:koin-androidx-compose:3.2.0")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //Gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
    // ROOM database
    // add the following lines
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:31.0.0"))
    // Declare the dependency for the Firestore library
    implementation("com.google.firebase:firebase-firestore-ktx")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.room:room-testing:$rootProject.roomVersion")

}