plugins {
    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlin.compose)

    //id("therouter")
    id("com.google.devtools.ksp")

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
}
ksp {
    arg("AROUTER_MODULE_NAME", project.name)
}

    dependencies {
        implementation("cn.therouter:router:1.3.2")
        ksp("cn.therouter:apt:1.3.2")

        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.core.ktx)
        implementation(libs.material)
        implementation("androidx.navigation:navigation-fragment-ktx:2.9.8")
        implementation("androidx.navigation:navigation-ui-ktx:2.9.8")
        implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")

        implementation("androidx.fragment:fragment-ktx:1.5.5")

        implementation("androidx.appcompat:appcompat:1.6.1")

        implementation("com.squareup.retrofit2:converter-gson:2.11.0")

        implementation("io.reactivex.rxjava3:rxjava:3.1.8")
        implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
        implementation("com.squareup.retrofit2:adapter-rxjava3:2.11.0")

        implementation("androidx.recyclerview:recyclerview:1.3.2")

        implementation("com.github.bumptech.glide:glide:5.0.5")

        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")


        implementation(project(":core:ui"))
        implementation(project(":core:net"))
        implementation(project(":core:data_store"))
        implementation(project(":core:utils"))

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(libs.androidx.junit)

    }
