package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.model.OfferStoriesModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun searchExclusiveOffersProduct(): Flow<ArrayList<OfferCardModel>>
    suspend fun searchOffersProduct(): Flow<ArrayList<OfferCardModel>>
    suspend fun searchStoriesOffers(): Flow<ArrayList<OfferStoriesModel>>
}