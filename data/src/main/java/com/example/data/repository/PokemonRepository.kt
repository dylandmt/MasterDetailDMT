package com.example.data.repository

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse
import com.example.data.datamodels.Sprites

interface PokemonRepository {

    suspend fun getPokemonList() : PokemonResponse?
    suspend fun getSprites(url:String) : Sprites?
}