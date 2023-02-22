package me.johngachihi.codestats.mobile.android

import android.app.Application
import me.johngachihi.codestats.mobile.android.data.datastore.prefsModule
import me.johngachihi.codestats.mobile.android.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(uiModule, prefsModule)
        }
    }
}