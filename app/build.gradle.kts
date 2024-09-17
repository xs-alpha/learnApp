import android.databinding.tool.writer.ViewBinding
import org.gradle.internal.impldep.com.fasterxml.jackson.core.JsonPointer.compile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.xiaosheng.learnapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xiaosheng.learnapp"
        minSdk = 21
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

//    启用view绑定
    buildFeatures{
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }


}

dependencies {

    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha5")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit-gtest:1.0.0-alpha02")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
//    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("de.hdodenhof:circleimageview:3.1.0")
//    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.github.youth5201314:banner:2.2.3")
    implementation("com.blankj:utilcodex:1.30.1")
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.16.1")
    implementation ("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation ("com.github.kittinunf.fuel:fuel-json:2.3.1")
    implementation ("com.google.code.gson:gson:2.10")
    // https://mvnrepository.com/artifact/io.reactivex/rxjava
//    implementation ("io.reactivex.rxjava2:rxkotlin:2.4.0")
//    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    // https://mvnrepository.com/artifact/io.reactivex/rxandroid
//    implementation("io.reactivex:rxandroid:1.2.1")
    implementation("androidx.room:room-runtime:2.2.5")
    annotationProcessor("androidx.room:room-compiler:2.2.5")
    // rxbus
    implementation ("com.hwangjr.rxbus:rxbus:3.0.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

//    //引入rxJava
//    implementation ("io.reactivex.rxjava2:rxjava:2.1.8")
//    //引入rxAndroid
//    implementation ("io.reactivex.rxjava2:rxandroid:2.0.1")
//    //引入rxJava适配器，方便rxJava与retrofit的结合
//    implementation ("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
//    //引入J神的rxrelay2，出现异常仍然可以处理
//    implementation ("com.jakewharton.rxrelay2:rxrelay:2.0.0")
}