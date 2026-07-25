plugins {
    alias(libs.plugins.android.library)
  //  alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
   // id("therouter")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.video"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {

        minSdk = 24


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}


ksp {
    arg("AROUTER_MODULE_NAME", project.name)
}


dependencies {
    implementation("cn.therouter:router:1.3.2")
    ksp("cn.therouter:apt:1.3.2")


    implementation("io.github.carguo:gsyvideoplayer-java:13.1.0")
    implementation("io.github.carguo:gsyvideoplayer:13.1.0")

    implementation(project(":core:ui"))
    implementation(project(":core:data_store"))
    implementation(project(":core:net"))
    implementation(project(":core:utils"))

    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.14.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}