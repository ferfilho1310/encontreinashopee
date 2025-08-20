package br.com.encontreinashopee.intent

sealed class SearchCupomsIntent {
    data object SearchCupoms: SearchCupomsIntent()
}