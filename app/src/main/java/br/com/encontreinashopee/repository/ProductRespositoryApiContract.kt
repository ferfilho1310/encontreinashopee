package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import kotlinx.coroutines.flow.Flow

interface ProductRespositoryApiContract {

    fun searchExclusiveOffers() : Flow<ArrayList<OfferCardModel>>

    fun searchOffers() : Flow<ArrayList<OfferCardModel>>
}