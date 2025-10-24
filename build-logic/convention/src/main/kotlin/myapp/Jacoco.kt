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
import java.util.Locale

val appExclusions = listOf(
    "demo/at/ram/presentation/designsystem/**", // Design System
    "demo/at/ram/presentation/designsystem/*.*", // Design System
    "demo/at/ram/presentation/ui/log/*.*", // Compose Log
    "demo/at/ram/presentation/AppCore.class",
    "demo/at/ram/presentation/RickAndMortyApplication.class",
)

val libExclusions = listOf(
    // Android generated classes
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/BR.*",
    "android/**/*.*",

    // AndroidX and Jetpack Compose generated
    "androidx/**/*.*",
    "**/*\$\$ExternalSyntheticLambda*.*",

    // Kotlin
    "**/*\$Companion.*",
    "**/*\$Companion\$*.*",
    "**/*\$WhenMappings.*",
    "**/*\$EntriesKt.*",
    "**/*\$inlined*.*",
    "**/*\$\$*.*",  // Compose compiler generated

    // Kotlin Serialization
    "**/*\$serializer.*",
    "**/*\$\$serializer.*",
    "**/*Serializer.*",

    // Dependency Injection - Hilt/Dagger
    "**/*_HiltComponents*.*",
    "**/*_HiltModules*.*",
    "**/*_Hilt*.*",
    "**/Hilt_*.*",
    "**/*_Factory.*",
    "**/*_Factory\$*.*",
    "**/*_MembersInjector.*",
    "**/*_MembersInjector\$*.*",
    "**/*Module_*.*",
    "**/*Dagger*.*",
    "**/*_Provide*Factory.*",
    "**/*_ComponentTreeDeps.*",
    "**/hilt_aggregated_deps/**",
    "**/dagger/**",
    "**/di/**",

    // DataBinding & ViewBinding
    "**/databinding/**",
    "**/DataBinderMapperImpl.*",
    "**/DataBindingComponent.*",
    "**/*Binding.*",
    "**/*BindingImpl.*",

    // Room Database generated
    "**/*_Impl.*",
    "**/*_Impl\$*.*",
    "**/*Database_Impl*.*",

    // Navigation generated
    "**/*Args.*",
    "**/*Args\$*.*",
    "**/*Directions.*",
    "**/*Directions\$*.*",

    // Moshi generated adapters
    "**/*JsonAdapter.*",
    "**/*JsonAdapter\$*.*",

    // Retrofit/OkHttp generated
    "**/*\$\$Factory.*",

    // Test files
    "**/*Test.*",
    "**/*Test\$*.*",
    "**/*Tests.*",
    "**/*Spec.*",
    "**/test/**",
    "**/androidTest/**",

    // BuildSrc/build-logic
    "**/buildSrc/**",
    "**/build-logic/**",
)

val coverageExclusions = libExclusions + appExclusions

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

/**
 * Creates a new task that generates a combined coverage report with data from local and
 * instrumented tests.
 *
 * `create{variant}CombinedCoverageReport`
 *
 * Note that coverage data must exist before running the task. This allows us to run device
 * tests on CI using a different Github Action or an external device farm.
 */
internal fun Project.configureJacoco(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
    withAndroidTest: Boolean = false,
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
            tasks.register(
                "create${variant.name.capitalize()}CombinedCoverageReport",
                JacocoReport::class,
            ) {
                dependsOn("test${variant.name.capitalize()}UnitTest")
                if (withAndroidTest) {
                    dependsOn("connected${variant.name.capitalize()}AndroidTest")
                }

                /** Debug jacoco
                doFirst {
                    logger.lifecycle("=== JaCoCo Coverage Report for ${variant.name} ===")
                    logger.lifecycle("Project: ${project.name}")

                    // Log JAR files
                    logger.lifecycle("\nJAR files (${allJars.get().size}):")
                    allJars.get().forEach { jar ->
                        logger.lifecycle("  - ${jar.asFile.absolutePath}")
                    }

                    // Log directories
                    logger.lifecycle("\nClass directories (${allDirectories.get().size}):")
                    allDirectories.get().forEach { dir ->
                        logger.lifecycle("  - ${dir.asFile.absolutePath}")
                    }

                    // Log source directories
                    logger.lifecycle("\nSource directories:")
                    sourceDirectories.files.forEach { source ->
                        logger.lifecycle("  - ${source.absolutePath}")
                    }

                    // Log execution data files
                    logger.lifecycle("\nExecution data files:")
                    executionData.files.forEach { execFile ->
                        if (execFile.exists()) {
                            logger.lifecycle("   ${execFile.absolutePath} (${execFile.length()} bytes)")
                        } else {
                            logger.lifecycle("   ${execFile.absolutePath} (NOT FOUND)")
                        }
                    }
                }
                */

                classDirectories.setFrom(
                    allJars.map { jars ->
                        jars.map { jar ->
                            project.zipTree(jar.asFile).matching {
                                exclude(coverageExclusions)
                            }
                        }
                    },
                    allDirectories.map { dirs ->
                        dirs.map { dir ->
                            myObjFactory.fileTree().setDir(dir).exclude(coverageExclusions)
                        }
                    },
                )

                reports {
                    xml.required = true
                    html.required = true
                    xml.outputLocation.set(
                        layout.buildDirectory.file("${rootDir}/build/reports/jacoco/${project.name}/${variant.name}/jacoco.xml")
                    )
                    html.outputLocation.set(
                        layout.buildDirectory.dir("${rootDir}/build/reports/jacoco/${project.name}/${variant.name}/html")
                    )
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

                    project.fileTree("$buildDir/outputs/code_coverage/${variant.name}AndroidTest")
                        .matching { include("**/*.ec") },
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
