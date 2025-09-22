package demo.at.ram.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import demo.at.ram.data.di.RepositoryModule
import demo.at.ram.domain.repository.CharacterRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCharacterRepository(
        fakeCharacterRepository: FakeCharacterRepository
    ): CharacterRepository
}