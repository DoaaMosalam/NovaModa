plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    //navigation
    id("androidx.navigation.safeargs.kotlin")
    //dagger hilt
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")

}
android {
    namespace = "com.holeCode.novamoda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.holeCode.novamoda"
        minSdk = 27
        targetSdk = 34
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
        forEach {
            it.buildConfigField(
                "String",
                "clientServerId",
                "\"77770924184-jamh047c6hbtjug0tbjvn9ucsul0vntv.apps.googleusercontent.com\""
            )
            it.resValue(
                "string",
                "facebook_app_id",
                "\"972229887956997\""
            )
            it.resValue(
                "string",
                "fb_login_protocol_scheme",
                "\"fb972229887956997\""
            )
            it.resValue(
                "string",
                "facebook_client_token",
                "\"c3d83b4eb9a8c04131447ce7471fecab\""
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
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.media3:media3-common:1.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //firebase
    implementation("com.google.firebase:firebase-bom:32.8.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation ("com.facebook.android:facebook-login:16.0.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-analytics:21.5.1")
    implementation("com.google.firebase:firebase-crashlytics:18.6.2")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")

    //splashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")
    // datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-kotlin-lite:4.26.0")
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //Glide library
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Picasso
    implementation("com.squareup.picasso:picasso:2.71828")
    // circle Image
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //reaction
    implementation("io.github.amrdeveloper:reactbutton:2.1.0")
    //View Model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    // ViewModel Factory
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //convert retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //OKHTTP
    //define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor")
    //volley android
    implementation("com.android.volley:volley:1.2.1")
    //navigation  components
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    //roomDatabase
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

// third party libraries
    implementation("com.github.pwittchen:reactivenetwork-rx2:3.0.8")
    // Dagger Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    //for Serializable annotation using in type converter
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.google.code.gson:gson:2.10.1")

//slider view
    implementation("com.github.smarteist:autoimageslider:1.4.0")
}