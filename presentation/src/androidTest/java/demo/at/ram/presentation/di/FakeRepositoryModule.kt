package demo.at.ram.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import demo.at.ram.data.di.RepositoryModule
import demo.at.ram.data.repository.CompositeCharacterRepositoryImpl
import demo.at.ram.data.repository.UserDataRepositoryImpl
import demo.at.ram.domain.repository.CharacterRepository
import demo.at.ram.domain.repository.CompositeCharacterRepository
import demo.at.ram.domain.repository.UserDataRepository
import javax.inject.Singleton

@Suppress("unused")
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

    @Binds
    abstract fun bindUserDataRepository(
        userDataRepo: FakeUserDataRepository
    ): UserDataRepository

    /**
     * use [CompositeCharacterRepositoryImpl] with [FakeCharacterRepository] and
     * [FakeUserDataRepository]
     */
    @Binds
    abstract fun bindCompositeCharacterRepository(
        compositeRepo: CompositeCharacterRepositoryImpl
    ): CompositeCharacterRepository
}