package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    val api: ProductRepositoryApi
) : ProductRepository {

    override suspend fun searchExclusiveOffersProduct(): Flow<ArrayList<OfferCardModel>> = api.searchExclusiveOffers()
    override suspend fun searchOffersProduct(): Flow<ArrayList<OfferCardModel>> = api.searchOffers()
}