plugins {
    alias(libs.plugins.android.library)
    //alias(libs.plugins.ksp)
    //id("com.android.legacy-kapt")
}

android {
    namespace = "com.example.feature"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions{
            annotationProcessorOptions{
                arguments["AROUTER_MODULE_NAME"]= project.name
            }
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
        debug{

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
//ksp {
//    arg("AROUTER_MODULE_NAME", project.name)
//}



dependencies {
    implementation("com.alibaba:arouter-api:1.5.2")
    annotationProcessor("com.alibaba:arouter-compiler:1.5.2")

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}