package br.com.encontreinashopee.di

import br.com.encontreinashopee.repository.cupoms.CupomApi
import br.com.encontreinashopee.repository.cupoms.CupomImpl
import br.com.encontreinashopee.repository.cupoms.CupomRepository
import br.com.encontreinashopee.repository.products.ProductRepository
import br.com.encontreinashopee.repository.products.ProductRepositoryApi
import br.com.encontreinashopee.repository.products.ProductRepositoryImpl
import br.com.encontreinashopee.retrofit.RetrofitClient
import br.com.encontreinashopee.track.OffersTrack
import br.com.encontreinashopee.track.OffersTrackContract
import br.com.encontreinashopee.viewmodel.CupomViewModel
import br.com.encontreinashopee.viewmodel.ProductViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object SearchProductsModule {

    val instance = module {
        single<FirebaseAnalytics> {
            FirebaseAnalytics.getInstance(get())
        }
        single<OffersTrackContract> { OffersTrack(get()) }

        viewModel { ProductViewModel(get(), get()) }
        viewModel { CupomViewModel(get(), get()) }

        single<ProductRepositoryApi> { RetrofitClient.createService() }
        single<ProductRepository> { ProductRepositoryImpl(get()) }

        single<CupomApi> { RetrofitClient.createService() }
        single<CupomRepository> { CupomImpl(get()) }
    }
}