package br.com.encontreinashopee.track

import br.com.encontreinashopee.model.CupomModel
import br.com.encontreinashopee.model.OfferCardModel

interface OffersTrackContract {

    fun trackClickShowOffers(model: OfferCardModel)
    fun trackClickCupons(model: CupomModel)
}