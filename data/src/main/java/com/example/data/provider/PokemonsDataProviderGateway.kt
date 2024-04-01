package com.example.data.provider

import com.example.data.datamodels.Pokemon

interface PokemonsDataProviderGateway {
    suspend fun getPokemonList() : ArrayList<Pokemon>

}