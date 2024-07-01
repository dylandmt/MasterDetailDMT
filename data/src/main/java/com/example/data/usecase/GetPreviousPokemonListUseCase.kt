package com.example.data.usecase

import com.example.data.datamodels.PokemonResponse
import com.example.data.repository.PokemonRepository
import org.koin.core.component.KoinComponent

class GetPreviousPokemonListUseCase( private val repository : PokemonRepository) : KoinComponent {
    suspend fun invoke(offset:Int,limit:Int) : PokemonResponse?= repository.getPreviousPokemonList(offset,limit)
}