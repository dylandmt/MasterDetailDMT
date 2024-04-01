package com.example.data.repository

import com.example.data.datamodels.Pokemon

interface PokemonRepository {

    fun getPokemonList() : ArrayList<Pokemon>
}