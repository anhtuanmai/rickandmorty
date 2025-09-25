package demo.at.ram.presentation.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import demo.at.ram.domain.error.AppError
import demo.at.ram.domain.error.ErrorMessageResolver
import timber.log.Timber
import javax.inject.Inject

class ErrorMessageResolverImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ErrorMessageResolver {

    override fun resolveErrorMessage(error: AppError): String {
        return try {
            context.getString(error.messageResId)
        } catch (e: Exception) {
            Timber.e(e)
            "An unexpected error occurred"
        }
    }
}
