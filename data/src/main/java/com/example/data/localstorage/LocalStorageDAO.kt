package com.example.data.localstorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.utils.Constants.Companion.FAVORITE_POKEMOS_TABLE

@Dao
interface LocalStorageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemonEntity: FavoritePokemonEntity)

    @Update
    fun update(pokemonEntity: FavoritePokemonEntity)

    @Delete
    fun delete(pokemonEntity: FavoritePokemonEntity)

    @Query("delete from $FAVORITE_POKEMOS_TABLE")
    fun deleteAll()

    @Query("select * from $FAVORITE_POKEMOS_TABLE")
    fun getAllPokemons():List<FavoritePokemonEntity>
}