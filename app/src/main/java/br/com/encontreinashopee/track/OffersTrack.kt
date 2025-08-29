package br.com.encontreinashopee.track

import br.com.encontreinashopee.model.CupomModel
import br.com.encontreinashopee.model.OfferCardModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class OffersTrack(
    val analitycs: FirebaseAnalytics
) : OffersTrackContract {

    override fun trackClickShowOffers(model: OfferCardModel) {
        analitycs.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, model.id.toString())
            param(FirebaseAnalytics.Param.ITEM_NAME, model.offerTitle.orEmpty())
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "Oferta")
        }
    }

    override fun trackClickCupons(model: CupomModel) {
        analitycs.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, model.id.toString())
            param(FirebaseAnalytics.Param.ITEM_NAME, "${model.title} - ${model.cupom}")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "Cupom")
        }
    }
}