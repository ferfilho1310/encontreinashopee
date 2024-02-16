package br.com.encontreinashopee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.repository.ProductRepository
import br.com.encontreinashopee.state.SearchProductDataState
import br.com.encontreinashopee.state.SearchProductExclusiveDataState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel(),
    ProductViewModelContract {

    val dataIntent = Channel<SearchProductDataIntent>(Channel.UNLIMITED)

    val dataState = MutableStateFlow<SearchProductDataState>(SearchProductDataState.Inactive)
    val dataStateExclusiveProduct = MutableStateFlow<SearchProductExclusiveDataState>(SearchProductExclusiveDataState.Inactive)

    init {
        handleEvents()
    }

    override fun searchOffers() {
        viewModelScope.launch {
            dataState.value = SearchProductDataState.Loading
            repository.searchOffersProduct()
                .onEach {
                    dataState.value = SearchProductDataState.ResponseData(it)
                }.catch {
                    dataState.value = SearchProductDataState.Error(it)
                }.launchIn(viewModelScope)
        }
    }

    override fun searchExclusiveOffers() {
        viewModelScope.launch {
            dataStateExclusiveProduct.value = SearchProductExclusiveDataState.Loading
            repository.searchExclusiveOffersProduct()
                .onEach {
                    dataStateExclusiveProduct.value = SearchProductExclusiveDataState.ResponseData(it)
                }.catch {
                    dataStateExclusiveProduct.value = SearchProductExclusiveDataState.Error(it)
                }.launchIn(viewModelScope)
        }
    }

    private fun handleEvents() {
        viewModelScope.launch {
            dataIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchProductDataIntent.SearchOffers -> searchOffers()
                    is SearchProductDataIntent.SearchOffersExclusive -> searchExclusiveOffers()
                }
            }
        }
    }
}