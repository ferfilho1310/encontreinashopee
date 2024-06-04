package br.com.encontreinashopee.viewmodel

import android.content.Context
import br.com.encontreinashopee.model.OfferCardModel

interface ProductViewModelContract {

    fun searchOffers()
    fun searchExclusiveOffers()
    fun clickOffer(model: OfferCardModel, context: Context)
}