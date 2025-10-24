package demo.at.ram.presentation.di

import demo.at.ram.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeUserDataRepository @Inject constructor() : UserDataRepository {

    val initFavorites = listOf<Long>(1,2,3)

    override fun getFavorites(): Flow<List<Long>> {
        return flow {
            emit(initFavorites)
        }
    }

    override suspend fun addFavorite(characterId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(characterId: Long) {
        TODO("Not yet implemented")
    }
}