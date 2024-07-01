package com.example.data.di

import android.app.Application
import com.example.data.localstorage.FavoritePokemonDatabase
import com.example.data.localstorage.LocalStorageDAO
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module

fun provideDatabase(application: Application) : FavoritePokemonDatabase = FavoritePokemonDatabase.getDatabaseInstance(application)
fun provideDao(database: FavoritePokemonDatabase) : LocalStorageDAO = database.localStorageDao()

val localStorageModule = module {
    single { provideDatabase(get()) }
    single { provideDao(get()) }
}