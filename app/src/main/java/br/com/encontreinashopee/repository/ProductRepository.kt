package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun searchExclusiveOffersProduct(): Flow<ArrayList<OfferCardModel>>
    suspend fun searchOffersProduct(): Flow<ArrayList<OfferCardModel>>
}