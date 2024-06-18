package br.com.encontreinashopee.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.repository.ProductRepository
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.tracker.OfferTrackerContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository,
    private val tracker: OfferTrackerContract
) : ViewModel(),
    ProductViewModelContract {

    val dataIntent = Channel<SearchProductDataIntent>(Channel.UNLIMITED)

    val dataState = MutableStateFlow<DataState>(DataState.Inactive)
    val dataStateExclusiveProduct =
        MutableStateFlow<DataState>(DataState.Inactive)
    val dataStateStories = MutableStateFlow<DataState>(DataState.Inactive)

    init {
        handleEvents()
    }

    override fun searchOffers() {
        viewModelScope.launch {
            dataState.value = DataState.Loading
            repository.searchOffersProduct()
                .onEach {
                    dataState.value = DataState.ResponseData(it)
                }.catch {
                    dataState.value = DataState.Error(it)
                }.launchIn(viewModelScope)
        }
    }

    override fun searchExclusiveOffers() {
        viewModelScope.launch {
            dataStateExclusiveProduct.value = DataState.Loading
            repository.searchExclusiveOffersProduct()
                .onEach {
                    dataStateExclusiveProduct.value =
                        DataState.ResponseData(it.sortedByDescending { it.id?.toInt() })
                }.catch {
                    dataStateExclusiveProduct.value = DataState.Error(it)
                }.launchIn(viewModelScope)
        }
    }

    override fun clickOffer(model: OfferCardModel, context: Context) {
        tracker.clickOfferTracker(model, context)
    }

    override fun searchStories() {
        viewModelScope.launch {
            dataStateStories.value = DataState.Loading
            repository.searchStoriesOffers()
                .onEach {
                    dataStateStories.value =
                        DataState.ResponseData(it)
                }.catch {
                    dataStateStories.value = DataState.Error(it)
                }.launchIn(viewModelScope)
        }
    }

    private fun handleEvents() {
        viewModelScope.launch {
            dataIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchProductDataIntent.SearchOffersExclusive -> searchExclusiveOffers()
                    is SearchProductDataIntent.ClickOffer -> {
                        clickOffer(it.model, it.context)
                    }
                    is SearchProductDataIntent.SearchStories -> searchStories()
                }
            }
        }
    }
}