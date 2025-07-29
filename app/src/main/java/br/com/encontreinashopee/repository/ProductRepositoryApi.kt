package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.repository.ProductRepositoryApi.KEY.OFFERS
import retrofit2.http.GET

interface ProductRepositoryApi  {

    @GET(OFFERS)
    suspend fun getOfferJson(): ArrayList<OfferCardModel>

    private object KEY {
        const val OFFERS = "ferfilho1310/encontreinashopeeoffers/main/offers.json"
    }
}