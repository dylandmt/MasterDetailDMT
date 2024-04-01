package com.example.masterdetaildmt

import android.app.Application
import com.example.masterdetaildmt.di.Module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(Module().appModule)
        }
    }
}