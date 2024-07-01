package com.example.data.usecase

import android.content.Context
import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonData
import com.example.data.datamodels.PokemonResponse
import com.example.data.localstorage.FavoritePokemonDatabase
import com.example.data.localstorage.FavoritePokemonEntity
import com.example.data.localstorage.LocalStorageDAO
import org.koin.core.component.KoinComponent

class AddNewFavoritePokemonUseCase (context: Context): KoinComponent {

    private var db : FavoritePokemonDatabase ? = null
    init {
        db = FavoritePokemonDatabase.getDatabaseInstance(context)
    }
    suspend fun invoke(pokemon: PokemonData) = db?.localStorageDao()?.insert(pokemonEntity = FavoritePokemonEntity(pokemon.name,pokemon.details.id))
}