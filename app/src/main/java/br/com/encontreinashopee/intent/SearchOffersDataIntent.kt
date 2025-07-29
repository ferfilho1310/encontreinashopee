package br.com.encontreinashopee.intent

sealed class SearchOffersDataIntent {
    data object SearchOffers: SearchOffersDataIntent()
}