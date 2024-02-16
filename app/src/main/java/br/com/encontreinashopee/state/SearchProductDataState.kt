package br.com.encontreinashopee.state

import br.com.encontreinashopee.model.OfferCardModel

sealed class SearchProductDataState {
    data object Inactive : SearchProductDataState()
    data object Loading : SearchProductDataState()
    data class ResponseData(val data: ArrayList<OfferCardModel>) : SearchProductDataState()
    data class Error(val error: Throwable?) : SearchProductDataState()
}

sealed class SearchProductExclusiveDataState {
    data object Inactive : SearchProductExclusiveDataState()
    data object Loading : SearchProductExclusiveDataState()
    data class ResponseData(val data: ArrayList<OfferCardModel>) : SearchProductExclusiveDataState()
    data class Error(val error: Throwable?) : SearchProductExclusiveDataState()
}