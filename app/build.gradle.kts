plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.microsl.loginsignup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.microsl.loginsignup"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.google.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Authenticate with Google
    implementation(libs.andoidx.credentials)
    implementation(libs.androidx.credential.play.services.auth)
    implementation(libs.googleid)

    // Import the Firebase BoM Dependencies
    implementation(platform(libs.firebase.bom)) // Firebase BOM
    implementation(libs.firebase.auth.ktx) // Firebase Authentication

    //implementation(libs.google.firebase.auth) // Firebase Authentication
    implementation(libs.google.firebase.database) // Firebase Database
    implementation(libs.firebase.firestore) // Firebase FireStore
    implementation(libs.firebase.analytics) // Firebase Analytics
    implementation(libs.firebase.storage) // Firebase Storage

    //implementation(libs.blurView)
    implementation ("com.github.Dimezis:BlurView:version-2.0.5") // BlurView for GlassMorphism

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

}