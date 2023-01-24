package me.johngachihi.codestats.mobile.android

import android.app.Application
//import me.johngachihi.codestats.mobile.modules
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
//            modules(modules)
        }
    }
}