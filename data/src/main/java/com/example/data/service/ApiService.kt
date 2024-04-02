package com.example.data.service

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse
import com.example.data.datamodels.Sprites
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemonList(): Response<PokemonResponse>

    @GET("pokemon/{url}/")
    suspend fun getSprites(@Path("url") url:String) : Response<Sprites>
}