plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.android.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.mannodermaus.android.junit5)
    alias(libs.plugins.room)
    alias(libs.plugins.apter.junit5)
    alias(libs.plugins.serialization)
    alias(libs.plugins.protobuf)
    jacoco
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
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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

jacoco {
    toolVersion = "0.8.8"
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents.beforeVariants {
    android.sourceSets.getByName(it.name) {
        val buildDir = layout.buildDirectory.get().asFile
        java.srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
        kotlin.srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
    }
}

dependencies {
    api(project(":domain"))

//    ksp(libs.hilt.compiler)
    kapt(libs.hilt.compiler)

//    ksp(libs.room.compiler)
    kapt(libs.room.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.room.runtime)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(libs.timber)
    implementation(libs.androidx.dataStore)
    implementation(libs.androidx.dataStore.core)
    api(libs.protobuf.kotlin.lite)
    implementation(libs.hilt.android)


    //Testing
    testImplementation(libs.turbine)
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

afterEvaluate {
    //EnableDynamicAgentLoading for Mockk
    tasks.named<Test>("testDebugUnitTest") {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("${layout.buildDirectory}/reports/jacoco/jacoco.xml"))
        html.outputLocation.set(file("${layout.buildDirectory}/reports/jacoco/html"))
    }

    val mainSrc = "$projectDir/src/main/java"
    sourceDirectories.setFrom(files(mainSrc))

    val exclusions = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/*\$WhenMappings.*",
        "**/*\$serializer.*",
        "**/*Screen*",
        "**/di/**/*.*",
        "**/databinding/**/*.*",
        "**/*_Factory.*"
    )

    classDirectories.setFrom(
        files(
            fileTree("$buildDir/tmp/kotlin-classes/debug") {
                exclude(exclusions)
            }
        )
    )

    executionData.setFrom(
        fileTree(buildDir) {
            include("**/*.exec")
        }
    )
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            limit {
                minimum = BigDecimal("0.80") // 80% coverage required
            }
        }
        rule {
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal("0.70")
            }
            excludes = listOf(
                "*.databinding.*",
                "*.BuildConfig",
                "*.*Test*"
            )
        }
    }
}