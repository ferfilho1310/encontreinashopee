package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.model.OfferStoriesModel
import br.com.encontreinashopee.model.OfferVideoModel
import kotlinx.coroutines.flow.Flow

interface ProductRespositoryApiContract {

    fun searchExclusiveOffers(): Flow<ArrayList<OfferCardModel>>
    fun searchOffers(): Flow<ArrayList<OfferCardModel>>
    fun searchStoriesOffers(): Flow<ArrayList<OfferStoriesModel>>
    fun searchVideoProductOffer(): Flow<ArrayList<OfferVideoModel>>
}