package com.example.data.repository

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse

interface PokemonRepository {

    suspend fun getPokemonList() : PokemonResponse?
}