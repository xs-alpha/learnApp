import android.databinding.tool.writer.ViewBinding

plugins {
    id("com.android.application")
}

android {
    namespace = "com.xiaosheng.learnapp"
    compileSdk = 31

    defaultConfig {
        applicationId = "com.xiaosheng.learnapp"
        minSdk = 21
        targetSdk = 31
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

//    启用view绑定
    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit-gtest:1.0.0-alpha02")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
//    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("de.hdodenhof:circleimageview:3.1.0")
//    implementation("io.coil-kt:coil-compose:2.7.0")
}