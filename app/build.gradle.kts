plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlin-android")
}
androidExtensions {
    isExperimental = true
}

android {
    compileSdkVersion (30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "com.dicoding.submission"
        minSdkVersion (22)
        targetSdkVersion (30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       /* javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.incremental" to "true")
            }
        }*/
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation ("androidx.core:core-ktx:1.3.2")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.2.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    //Room
    implementation ("androidx.room:room-runtime:2.2.5")
    implementation ("androidx.room:room-ktx:2.2.5")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

	kapt ("androidx.room:room-compiler:2.2.5")

    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation ("com.google.android.material:material:1.2.1")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    implementation ("io.reactivex.rxjava2:rxjava:2.1.16")
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.2")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    implementation ("androidx.recyclerview:recyclerview:1.1.0")

    implementation ("com.github.bumptech.glide:glide:4.11.0")
    kapt ("com.github.bumptech.glide:compiler:4.11.0")


    testImplementation ("junit:junit:4.13.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
}