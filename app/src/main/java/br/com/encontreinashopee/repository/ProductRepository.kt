package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getOffers(): Flow<List<OfferCardModel>>
}