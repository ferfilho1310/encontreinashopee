package br.com.encontreinashopee.repository.cupoms

import br.com.encontreinashopee.model.CupomModel
import retrofit2.http.GET

interface CupomApi {

    @GET(URL.CUPOM_URL)
    suspend fun getCupom(): List<CupomModel>

    object URL {
        const val CUPOM_URL = "ferfilho1310/encontreinashopeeoffers/main/cupons.json"
    }
}