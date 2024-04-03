package com.example.data.service

import com.example.data.datamodels.PokemonResponse
import com.example.data.datamodels.PokemonDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemonList(): Response<PokemonResponse>

    @GET("pokemon/{url}")
    suspend fun getSprites(@Path("url") url:String) : Response<PokemonDetails>

    @GET("pokemon")
    suspend fun getNextPokemonList(@Query("offset") offset:Int, @Query("limit") limit:Int): Response<PokemonResponse>
}