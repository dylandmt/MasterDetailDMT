package com.example.data.implementation

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonDetails
import com.example.data.datamodels.PokemonResponse
import com.example.data.repository.PokemonRepository
import com.example.data.service.ApiService
import com.example.data.viewmodel.LoadingComponentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class PokemonRepositoryImpl(private val service: ApiService, private val spinner:LoadingComponentViewModel) : PokemonRepository, KoinComponent {
    private var pokemonList: ArrayList<Pokemon>? = arrayListOf()
    override fun getPokemonDataLoaded(): ArrayList<Pokemon>? {
        return pokemonList
    }

    override suspend fun getPokemonList(): PokemonResponse? {
        return try {
            spinner.showLoading(true)
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }
            val response = service.getPokemonList()
            if (response.isSuccessful){
                response.body()?.results.let {
                    pokemonList = it
                }

                spinner.showLoading(false)
                response.body()
            } else{

                spinner.showLoading(false)
                null
            }
        } catch (e:Exception){
            spinner.showLoading(false)
            null
        }
    }

    override suspend fun getSprites(url:String): PokemonDetails? {
        return try {
            spinner.showLoading(true)
            val response = service.getSprites(url)
            if (response.isSuccessful){
                spinner.showLoading(false)
                response.body()
            } else{
                response.message()
                spinner.showLoading(false)
                null
            }
        } catch (e:Exception){
            spinner.showLoading(false)
            null
        }
    }

    override suspend fun getNextPokemonList(offset:Int,limit:Int): PokemonResponse? {
        return try {
            spinner.showLoading(true)
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }
            val response = service.getNextPokemonList(offset, limit)
            if (response.isSuccessful){
                spinner.showLoading(false)
                response.body()
            } else{
                spinner.showLoading(false)
                null
            }
        } catch (e:Exception){
            spinner.showLoading(false)
            null
        }
    }

    override suspend fun getPreviousPokemonList(offset: Int, limit: Int): PokemonResponse? {
        return try {
            spinner.showLoading(true)
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
            }
            val response = service.getPreviousPokemonList(offset, limit)
            if (response.isSuccessful){
                spinner.showLoading(false)
                response.body()
            } else{
                spinner.showLoading(false)
                null
            }
        } catch (e:Exception){
            spinner.showLoading(false)
            null
        }
    }
}