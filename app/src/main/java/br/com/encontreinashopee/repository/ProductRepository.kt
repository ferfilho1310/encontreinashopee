package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.model.OfferStoriesModel
import br.com.encontreinashopee.model.OfferVideoModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun searchExclusiveOffersProduct(): Flow<ArrayList<OfferCardModel>>
    suspend fun searchOffersProduct(): Flow<ArrayList<OfferCardModel>>
    suspend fun searchStoriesOffers(): Flow<ArrayList<OfferStoriesModel>>
    suspend fun searchOfferVideo(): Flow<ArrayList<OfferVideoModel>>
}