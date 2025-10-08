// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("jacocoVersion", "0.8.8")
    }

    dependencies {
        classpath("org.jacoco:org.jacoco.core:${property("jacocoVersion")}")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.android.ksp) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    jacoco
}