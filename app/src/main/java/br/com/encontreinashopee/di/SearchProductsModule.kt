package br.com.encontreinashopee.di

import br.com.encontreinashopee.repository.ProductRepository
import br.com.encontreinashopee.repository.ProductRepositoryApi
import br.com.encontreinashopee.repository.ProductRepositoryImpl
import br.com.encontreinashopee.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object SearchProductsModule {

    val instance = module {

        viewModel { ProductViewModel(get()) }

        single { ProductRepositoryApi() }
        single<ProductRepository> { ProductRepositoryImpl(get()) }
    }

}