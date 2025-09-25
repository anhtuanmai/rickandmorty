package demo.at.ram.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import demo.at.ram.domain.error.ErrorMessageResolver
import demo.at.ram.presentation.util.ErrorMessageResolverImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {

    @Binds
    abstract fun bindErrorMessageResolver(
        errorMessageResolverImpl: ErrorMessageResolverImpl
    ): ErrorMessageResolver
}
