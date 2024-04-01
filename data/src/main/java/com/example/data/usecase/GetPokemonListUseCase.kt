package com.example.data.usecase

import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonResponse
import com.example.data.repository.PokemonRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPokemonListUseCase(private val repository : PokemonRepository) : KoinComponent {
    suspend fun invoke() : PokemonResponse ?= repository.getPokemonList()
}