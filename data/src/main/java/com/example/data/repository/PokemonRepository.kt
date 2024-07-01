package com.example.data.repository

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse
import com.example.data.datamodels.PokemonDetails

interface PokemonRepository {



    fun getPokemonDataLoaded() : ArrayList<Pokemon>?
    suspend fun getPokemonList() : PokemonResponse?
    suspend fun getSprites(url:String) : PokemonDetails?
    suspend fun getNextPokemonList(offset:Int,limit:Int) : PokemonResponse?
    suspend fun getPreviousPokemonList(offset:Int,limit:Int) : PokemonResponse?
}