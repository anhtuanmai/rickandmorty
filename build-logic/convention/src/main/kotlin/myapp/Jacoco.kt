package myapp

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.api.variant.SourceDirectories
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

private val exclusions = listOf(
    // Android
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "android/**/*.*",

    // Hilt/Dagger
    "**/*_Hilt*.*",
    "**/Hilt_*.*",
    "**/hilt_aggregated_deps/**",
    "**/*_Factory.*",
    "**/*_MembersInjector.*",
    "**/*Module_*.*",

    // DataBinding
    "**/databinding/**",
    "**/BR.*",

    // Kotlin
    "**/*\$WhenMappings.*",
    "**/*\$serializer.*",

    // DI (if appropriate for your project)
    "**/di/**",

    // Test classes only (not production classes with "Test" in name)
    "**/*Test.class",
    "**/*Tests.class",
)

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString()
}

internal fun Project.configureJacoco(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    androidComponentsExtension.onVariants { variant ->
        val myObjFactory = project.objects
        val buildDir = layout.buildDirectory.get().asFile
        val allJars: ListProperty<RegularFile> = myObjFactory.listProperty(RegularFile::class.java)
        val allDirectories: ListProperty<Directory> =
            myObjFactory.listProperty(Directory::class.java)
        val reportTask =
            tasks.register<JacocoReport>(
                "create${variant.name.capitalize()}CoverageReport"
            ) {
                dependsOn("testDebugUnitTest")

                classDirectories.setFrom(
                    allJars,
                    allDirectories.map { dirs ->
                        dirs.map { dir ->
                            myObjFactory.fileTree().setDir(dir).exclude(exclusions)
                        }
                    },
                )

                reports {
                    xml.required = true
                    html.required = true
                    xml.outputLocation.set(file("${layout.buildDirectory}/reports/jacoco/jacoco.xml"))
                    html.outputLocation.set(file("${layout.buildDirectory}/reports/jacoco/html"))
                }

                fun SourceDirectories.Flat?.toFilePaths(): Provider<List<String>> = this
                    ?.all
                    ?.map { directories -> directories.map { it.asFile.path } }
                    ?: provider { emptyList() }
                sourceDirectories.setFrom(
                    files(
                        variant.sources.java.toFilePaths(),
                        variant.sources.kotlin.toFilePaths()
                    ),
                )

                executionData.setFrom(
                    project.fileTree("$buildDir/outputs/unit_test_code_coverage/${variant.name}UnitTest")
                        .matching { include("**/*.exec") },
                )
            }


        variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
            .use(reportTask)
            .toGet(
                ScopedArtifact.CLASSES,
                { _ -> allJars },
                { _ -> allDirectories },
            )
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            // Required for JaCoCo + Robolectric
            // https://github.com/robolectric/robolectric/issues/2230
            isIncludeNoLocationClasses = true

            // Required for JDK 11 with the above
            // https://github.com/gradle/gradle/issues/5184#issuecomment-391982009
            excludes = listOf("jdk.internal.*")
        }
    }
}
