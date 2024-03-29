package br.com.encontreinashopee.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class EncontreinaShopeeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EncontreinaShopeeApplication)
            androidLogger(Level.NONE)
            modules(
                listOf(
                    SearchProductsModule.instance
                )
            )
        }
    }
}