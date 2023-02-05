plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}
android {

    compileSdk = 33

    defaultConfig {
        applicationId = "com.arteneta.githubconsumerapp"
        minSdk = 24
        targetSdk =  33
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = ".deb"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn"
        )
        //useIR = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
	val composeVersion = "1.4.0-alpha04"
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.appcompat:appcompat:1.6.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // compose
    implementation ("androidx.compose.foundation:foundation:$composeVersion")
    implementation ("androidx.compose.material3:material3:1.1.0-alpha05")
    implementation ("androidx.compose.material:material:1.4.0-alpha05")
    implementation ("androidx.compose.material3:material3-window-size-class:1.1.0-alpha05")
    implementation ("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation ("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation ("androidx.compose.ui:ui:$composeVersion")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation ("androidx.activity:activity-compose:1.6.1")

    implementation ("androidx.compose.material:material-icons-extended:1.3.1")
    implementation ("com.google.dagger:hilt-android:2.44.2")
    kapt ("com.google.dagger:hilt-compiler:2.44.2")


    val accompanistVersion = "0.29.1-alpha"

    implementation ("com.google.accompanist:accompanist-navigation-material:$accompanistVersion")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation ("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")
    implementation ("com.google.accompanist:accompanist-webview:$accompanistVersion")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
}