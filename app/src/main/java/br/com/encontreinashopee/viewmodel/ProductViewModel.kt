package br.com.encontreinashopee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.encontreinashopee.intent.SearchOffersDataIntent
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.repository.ProductRepository
import br.com.encontreinashopee.state.DataState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository,
) : ViewModel() {

    val dataIntent = Channel<SearchOffersDataIntent>(Channel.UNLIMITED)
    private val _dataState = MutableStateFlow<DataState>(DataState.Loading)
    val dataState: StateFlow<DataState> = _dataState

    private var cache: List<OfferCardModel>? = null

    init {
        handleEvents()
    }

    private fun handleEvents() {
        viewModelScope.launch {
            dataIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchOffersDataIntent.SearchOffers -> searchOffers()
                }
            }
        }
    }

    private fun searchOffers() {
        if (cache != null) {
            _dataState.value = DataState.ResponseData(cache)
            return
        }

        viewModelScope.launch {
            _dataState.value = DataState.Loading
            repository.getOffers()
                .catch {
                    _dataState.value = DataState.Error("Error: " + it.message)
                }.collect {
                    cache = it
                    _dataState.value = DataState.ResponseData(it.sortedByDescending { it.id })
                }
        }
    }
}