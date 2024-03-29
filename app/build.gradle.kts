import org.apache.tools.ant.util.JavaEnvUtils.VERSION_11

plugins {
	id("com.android.application")
	id("kotlin-android")
	id("kotlin-kapt")
	id("kotlin-parcelize")
	id("com.google.dagger.hilt.android")
	id ("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
}

android {
	compileSdk = 33
	//buildToolsVersion = "33.0.0"

	defaultConfig {
		applicationId = "com.arteneta.githubapp"
		minSdk = 24
		targetSdk= 33
		versionCode = 2
		versionName = "2.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		/* javaCompileOptions {
			 annotationProcessorOptions {
				 arguments = mapOf("room.incremental" to "true")
			 }
		 }*/
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		/*getByName("debug") {
			applicationIdSuffix = ".debug"
			versionNameSuffix = ".deb"
		}*/
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

	implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
	val composeVersion = "1.4.0-alpha04"

	implementation("androidx.core:core-ktx:1.9.0")
	implementation("androidx.appcompat:appcompat:1.6.0")
		//implementation("com.google.android.material:material:1.7.0")
	//implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	//implementation("androidx.preference:preference-ktx:1.2.0")
	//implementation("androidx.cardview:cardview:1.0.0")

	implementation("androidx.datastore:datastore-preferences:1.0.0")
	//Room
	implementation("androidx.room:room-runtime:2.5.0")
	implementation("androidx.room:room-ktx:2.5.0")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
	//implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

	kapt("androidx.room:room-compiler:2.5.0")

	//implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
	//implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
	//implementation("androidx.legacy:legacy-support-v4:1.0.0")

	implementation("io.reactivex.rxjava2:rxjava:2.2.21")
	implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

	//retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
	implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

	//implementation("androidx.recyclerview:recyclerview:1.2.1")

	//implementation("com.github.bumptech.glide:glide:4.14.2")
	//kapt("com.github.bumptech.glide:compiler:4.14.2")

	implementation("androidx.work:work-runtime-ktx:2.7.1")
	implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
	//gson
	implementation ("com.google.code.gson:gson:2.10.1")

	// hilt
	implementation("com.google.dagger:hilt-android:2.44.2")
	kapt("com.google.dagger:hilt-android-compiler:2.44.2")

	//koil
	implementation("io.coil-kt:coil-compose:2.2.2")

	// compose
	implementation ("androidx.compose.foundation:foundation:$composeVersion")
	implementation ("androidx.compose.material3:material3:1.1.0-alpha04")
	implementation ("androidx.compose.material3:material3-window-size-class:1.1.0-alpha04")
	implementation ("androidx.compose.runtime:runtime-livedata:$composeVersion")
	implementation ("androidx.compose.ui:ui-tooling-preview:$composeVersion")
	implementation ("androidx.compose.ui:ui:$composeVersion")
	implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
	implementation ("androidx.navigation:navigation-compose:2.5.3")
	implementation ("androidx.activity:activity-compose:1.6.1")
	implementation ("androidx.compose.material:material:1.4.0-alpha04")
	implementation ("androidx.compose.material:material-icons-extended:1.3.1")
	implementation ("com.google.dagger:hilt-android:2.44.2")
	kapt ("com.google.dagger:hilt-compiler:2.44.2")

	//glance
	implementation ("androidx.glance:glance-appwidget:1.0.0-alpha05")

	//accompanist
	val accompanistVersion = "0.29.0-alpha"

	implementation ("com.google.accompanist:accompanist-navigation-material:$accompanistVersion")
	implementation ("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
	implementation ("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")
	implementation ("com.google.accompanist:accompanist-placeholder:$accompanistVersion")
	implementation ("com.google.accompanist:accompanist-drawablepainter:$accompanistVersion")

	//data store
	implementation("androidx.datastore:datastore-preferences:1.0.0")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	debugImplementation ("androidx.compose.ui:ui-tooling:1.3.3")
}