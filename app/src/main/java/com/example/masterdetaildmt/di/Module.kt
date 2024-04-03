package com.example.masterdetaildmt.di

import com.example.data.viewmodel.DetailsViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
class Module {
    val appModule = module {
        viewModel { DetailsViewModel() }
    }
}