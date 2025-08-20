package br.com.encontreinashopee.repository.products

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.repository.products.ProductRepositoryApi.KEY.OFFERS
import retrofit2.http.GET

interface ProductRepositoryApi  {

    @GET(OFFERS)
    suspend fun getOfferJson(): ArrayList<OfferCardModel>

    private object KEY {
        const val OFFERS = "ferfilho1310/encontreinashopeeoffers/main/offers.json"
    }
}