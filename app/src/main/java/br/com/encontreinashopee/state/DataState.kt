package br.com.encontreinashopee.state

sealed class DataState {
    data object Loading : DataState()
    data class ResponseData<T>(val data: T) : DataState()
    data class Error(val error: String?) : DataState()
}