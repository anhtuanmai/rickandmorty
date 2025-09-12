package demo.at.ram.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import demo.at.ram.data.source.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM ${CharacterEntity.TABLE_NAME} ORDER BY id ASC")
    suspend fun getAll(): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<CharacterEntity>) : List<Long>
}