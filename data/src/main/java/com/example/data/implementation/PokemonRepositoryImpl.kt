package com.example.data.implementation

import com.example.data.datamodels.Pokemon
import com.example.data.repository.PokemonRepository
import org.koin.core.component.KoinComponent

class PokemonRepositoryImpl : PokemonRepository, KoinComponent {
    override fun getPokemonList(): ArrayList<Pokemon> {
        return arrayListOf()
    }
}