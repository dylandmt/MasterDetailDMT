package com.example.data.implementation

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse
import com.example.data.datamodels.PokemonDetails
import com.example.data.repository.PokemonRepository
import com.example.data.service.ApiService
import org.koin.core.component.KoinComponent
import retrofit2.http.Query

class PokemonRepositoryImpl(private val service: ApiService) : PokemonRepository, KoinComponent {
    private var pokemonList: ArrayList<Pokemon>? = arrayListOf()
    override fun getPokemonDataLoaded(): ArrayList<Pokemon>? {
        return pokemonList
    }

    override suspend fun getPokemonList(): PokemonResponse? {
        return try {
            val response = service.getPokemonList()
            if (response.isSuccessful){
                response.body()?.results.let {
                    pokemonList = it
                }
                response.body()
            } else{
                null
            }
        } catch (e:Exception){
            null
        }
    }

    override suspend fun getSprites(url:String): PokemonDetails? {
        return try {
            val response = service.getSprites(url)
            if (response.isSuccessful){
                response.body()
            } else{

                response.message()
                null
            }
        } catch (e:Exception){
            null
        }
    }

    override suspend fun getNextPokemonList(offset:Int,limit:Int): PokemonResponse? {
        return try {
            val response = service.getNextPokemonList(offset, limit)
            if (response.isSuccessful){
                response.body()
            } else{
                null
            }
        } catch (e:Exception){
            null
        }
    }

    override suspend fun getPreviousPokemonList(offset: Int, limit: Int): PokemonResponse? {
        return try {
            val response = service.getPreviousPokemonList(offset, limit)
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