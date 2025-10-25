package demo.at.ram.shared.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
annotation class ExcludeFromJacocoGeneratedReport