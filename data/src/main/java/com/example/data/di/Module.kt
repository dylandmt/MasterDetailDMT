package com.example.data.di

import com.example.data.implementation.PokemonRepositoryImpl
import com.example.data.repository.PokemonRepository
import com.example.data.usecase.GetNextPokemonListUseCase
import com.example.data.usecase.GetPokemonListUseCase
import com.example.data.usecase.GetPokemonSpritesUseCase
import com.example.data.usecase.GetPreviousPokemonListUseCase
import com.example.data.viewmodel.DetailsViewModel
import com.example.data.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            repositoryModule,
            useCaseModule,
            viewModelModule, networkModule
        )
    )
}


val repositoryModule = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonSpritesUseCase(get()) }
    factory { GetNextPokemonListUseCase(get()) }
    factory { GetPreviousPokemonListUseCase(get()) }
}

val viewModelModule = module {
    single { HomeViewModel(get(), get(), get(), get()) }
    viewModel { DetailsViewModel() }
}
