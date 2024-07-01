package com.example.data.usecase;

import android.content.Context
import com.example.data.localstorage.FavoritePokemonDatabase
import com.example.data.localstorage.FavoritePokemonEntity
import org.koin.core.component.KoinComponent

class GetAllFavoritePokemonListUseCase(context: Context): KoinComponent {

        private var db : FavoritePokemonDatabase? = null
        init {
                db = FavoritePokemonDatabase.getDatabaseInstance(context)
        }
         fun invoke(): List<FavoritePokemonEntity>? = db?.localStorageDao()?.getAllPokemons()
}