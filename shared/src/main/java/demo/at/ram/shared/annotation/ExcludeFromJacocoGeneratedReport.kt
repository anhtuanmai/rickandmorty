package demo.at.ram.shared.annotation

/**
 * Use to exclude Preview code in Jacoco coverage
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
annotation class ExcludeFromJacocoGeneratedReport