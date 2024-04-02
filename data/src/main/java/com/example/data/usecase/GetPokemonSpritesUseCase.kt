package com.example.data.usecase

import com.example.data.datamodels.PokemonResponse
import com.example.data.datamodels.Sprites
import com.example.data.repository.PokemonRepository
import org.koin.core.component.KoinComponent

class GetPokemonSpritesUseCase (private val repository : PokemonRepository) : KoinComponent {
        suspend fun invoke(url:String) : Sprites?= repository.getSprites(url)

}