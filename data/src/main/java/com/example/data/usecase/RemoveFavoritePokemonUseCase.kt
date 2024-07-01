package com.example.data.usecase

import android.content.Context
import com.example.data.datamodels.PokemonData
import com.example.data.localstorage.FavoritePokemonDatabase
import com.example.data.localstorage.FavoritePokemonEntity
import org.koin.core.component.KoinComponent

class RemoveFavoritePokemonUseCase (context: Context): KoinComponent {

    private var db : FavoritePokemonDatabase? = null
    init {
        db = FavoritePokemonDatabase.getDatabaseInstance(context)
    }
    fun invoke(pokemon: PokemonData) = db?.localStorageDao()?.delete(pokemonEntity = FavoritePokemonEntity(pokemon.name,pokemon.details.id))
}