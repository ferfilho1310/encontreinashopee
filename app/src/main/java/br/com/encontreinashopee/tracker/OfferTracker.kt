package br.com.encontreinashopee.tracker

import android.content.Context
import android.os.Bundle
import br.com.encontreinashopee.model.OfferCardModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class OfferTracker : OfferTrackerContract {
    override fun clickOfferTracker(model: OfferCardModel, context: Context) {
        val analytics = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Oferta")
            putString(FirebaseAnalytics.Param.ITEM_ID, model.id.orEmpty())
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, model.offerTitle.orEmpty())
        }

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun clickNavegateOffer(model: OfferCardModel, context: Context) {
        val analytics = FirebaseAnalytics.getInstance(context)
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "Detailhes da Oferta")
            param(FirebaseAnalytics.Param.ITEM_ID, model.id.orEmpty())
            param(FirebaseAnalytics.Param.CONTENT_TYPE, model.offerTitle.orEmpty())
        }
    }
}