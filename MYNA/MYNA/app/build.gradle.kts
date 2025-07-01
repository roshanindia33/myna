val razorpayKeyId = project.findProperty("RAZORPAY_KEY_ID") as? String ?: ""
val razorpayKeySecret = project.findProperty("RAZORPAY_KEY_SECRET") as? String ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.myna.myna"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.myna.myna"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "RAZORPAY_KEY_ID", "\"${razorpayKeyId}\"")
        buildConfigField("String", "RAZORPAY_KEY_SECRET", "\"${razorpayKeySecret}\"")
    }
    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = "your-store-password"
            keyAlias = "mynaKey"
            keyPassword = "your-key-password"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.annotation)
    implementation(libs.coordinatorlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.cardview)
    implementation(libs.recyclerview)
    implementation(libs.razorpay)

    implementation(platform(libs.firebase.bom))

//    firebase services
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
//    for growth chart
    implementation (libs.mpandroidchart)

}