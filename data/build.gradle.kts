plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.android.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.mannodermaus.android.junit5)
    alias(libs.plugins.room)
    alias(libs.plugins.apter.junit5)
    alias(libs.plugins.serialization)
}

android {
    namespace = "demo.at.ram.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.gson)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.junit.jupiter.api)
    androidTestImplementation(libs.junit.jupiter.engine)
    androidTestImplementation(libs.androidx.espresso.core)
}

//EnableDynamicAgentLoading for Mockk
afterEvaluate {
    tasks.named<Test>("testDebugUnitTest") {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading")
    }
}

