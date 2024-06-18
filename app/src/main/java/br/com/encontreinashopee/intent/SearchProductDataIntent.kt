package br.com.encontreinashopee.intent

import android.content.Context
import br.com.encontreinashopee.model.OfferCardModel

sealed class SearchProductDataIntent {
    data object SearchOffersExclusive : SearchProductDataIntent()
    data class ClickOffer(val model: OfferCardModel, val context: Context): SearchProductDataIntent()
    data object SearchStories: SearchProductDataIntent()
}