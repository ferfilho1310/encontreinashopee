package br.com.encontreinashopee.intent

sealed class SearchProductDataIntent {

    data object SearchOffersExclusive : SearchProductDataIntent()
    data object SearchOffers : SearchProductDataIntent()

}