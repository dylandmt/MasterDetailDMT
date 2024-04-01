package com.example.data.service

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemonList(): Response<PokemonResponse>
}