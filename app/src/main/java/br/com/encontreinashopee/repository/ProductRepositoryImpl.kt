package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.model.OfferStoriesModel
import br.com.encontreinashopee.model.OfferVideoModel
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    val api: ProductRepositoryApi
) : ProductRepository {

    override suspend fun searchExclusiveOffersProduct(): Flow<ArrayList<OfferCardModel>> = api.searchExclusiveOffers()
    override suspend fun searchOffersProduct(): Flow<ArrayList<OfferCardModel>> = api.searchOffers()
    override suspend fun searchStoriesOffers(): Flow<ArrayList<OfferStoriesModel>> = api.searchStoriesOffers()
    override suspend fun searchOfferVideo(): Flow<ArrayList<OfferVideoModel>> = api.searchVideoProductOffer()
}