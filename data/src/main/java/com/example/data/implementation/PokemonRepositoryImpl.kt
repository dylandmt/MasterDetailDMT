package com.example.data.implementation

import com.example.data.datamodels.PokemonResponse
import com.example.data.repository.PokemonRepository
import com.example.data.service.ApiService
import org.koin.core.component.KoinComponent

class PokemonRepositoryImpl(private val service: ApiService) : PokemonRepository, KoinComponent {
    override suspend fun getPokemonList(): PokemonResponse? {
        return try {
            val response = service.getPokemonList()
            if (response.isSuccessful){
                response.body()
            } else{
                null
            }
        } catch (e:Exception){
            null
        }
    }
}