// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

// Unit tests only
tasks.register("runAllUnitTests") {
    group = "verification"
    description = "Runs all unit tests for all modules"

    dependsOn(provider {
        subprojects.mapNotNull { project ->
            project.tasks.findByName("testDebugUnitTest") ?: project.tasks.findByName("test")
        }
    })
}

// Instrumented tests only
tasks.register("runAllInstrumentedTests") {
    group = "verification"
    description = "Runs all instrumented tests for all modules"

    dependsOn(provider {
        subprojects.mapNotNull { project ->
            project.tasks.findByName("connectedDebugAndroidTest")
        }
    })
}

// Combined task
tasks.register("runAllTests") {
    group = "verification"
    description = "Runs all unit and instrumented tests for all modules"

    dependsOn("runAllUnitTests")
    // Only depend on instrumented tests if you have devices connected
    dependsOn("runAllInstrumentedTests")
}
