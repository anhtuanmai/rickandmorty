package demo.at.ram.domain.error

interface ErrorMessageResolver {
    fun resolveErrorMessage(error: AppError): String
}