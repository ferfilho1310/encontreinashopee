package br.com.encontreinashopee.state

import br.com.encontreinashopee.model.OfferCardModel

sealed class DataState {
    data object Inactive : DataState()
    data object Loading : DataState()
    data class ResponseData<T>(val data: T) : DataState()
    data class Error(val error: Throwable?) : DataState()
}