// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

// Unit tests only
tasks.register<TestReport>("runAllUnitTests") {
    group = "verification"
    description = "Runs all unit tests for all modules and generates aggregated HTML report"

    // Set the destination for the aggregated report
    destinationDirectory = layout.buildDirectory.dir("reports/tests/allUnitTests")

    // Collect all test tasks from subprojects
    val unitTestTasks = subprojects.mapNotNull { project ->
        project.tasks.findByName("testDebugUnitTest") as? Test ?:
        project.tasks.findByName("test") as? Test
    }

    // Run all unit tests and aggregate results
    dependsOn(unitTestTasks)
    testResults.from(unitTestTasks)

    doLast {
        println("Aggregated test report available at: ${destinationDirectory.get()}/index.html")
    }
}

// Instrumented tests only
tasks.register("runAllInstrumentedTests") {
    group = "verification"
    description = "Runs all instrumented tests for all modules"

    val instrumentedTestTasks = subprojects.mapNotNull { project ->
        project.tasks.findByName("connectedDebugAndroidTest") ?:
        project.tasks.findByName("connectedAndroidTest")
    }

    dependsOn(instrumentedTestTasks)

    doLast {
        println("All instrumented tests completed!")
        println()

        subprojects.forEach { project ->
            val androidTestReportDir = project.layout.buildDirectory.dir("reports/androidTests/connected/debug").get().asFile
            val indexFile = File(androidTestReportDir, "index.html")

            if (indexFile.exists()) {
                println("Report [${project.name}] : ${indexFile.absolutePath}")
            }
        }
        println()
    }
}

// Combined task
tasks.register("runAllTests") {
    group = "verification"
    description = "Runs all unit and instrumented tests for all modules"

    dependsOn("runAllUnitTests")
    // Only depend on instrumented tests if you have devices connected
    dependsOn("runAllInstrumentedTests")
}
