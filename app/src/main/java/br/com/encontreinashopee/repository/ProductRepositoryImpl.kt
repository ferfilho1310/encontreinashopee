package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    val api: ProductRepositoryApi
) : ProductRepository {
    override suspend fun getOffers(): Flow<List<OfferCardModel>> = flow {
            emit(api.getOfferJson() as List<OfferCardModel>)
    }
}