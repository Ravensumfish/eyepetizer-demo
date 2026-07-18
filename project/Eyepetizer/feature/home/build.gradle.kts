plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.home"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
        viewBinding{
            enable = true
        }
    }

    defaultConfig {
        applicationId = "com.example.home"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
}

    dependencies {
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.core.ktx)
        implementation(libs.material)
        implementation("androidx.navigation:navigation-fragment-ktx:2.9.8")
        implementation("androidx.navigation:navigation-ui-ktx:2.9.8")

        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        implementation("io.reactivex.rxjava3:rxjava:3.1.8")
        implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
        implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

        implementation("androidx.recyclerview:recyclerview:1.3.2")

        implementation("com.github.bumptech.glide:glide:5.0.5")

        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")


        implementation(project(":core:ui"))
        implementation(project(":core:net"))

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(libs.androidx.junit)

    }
