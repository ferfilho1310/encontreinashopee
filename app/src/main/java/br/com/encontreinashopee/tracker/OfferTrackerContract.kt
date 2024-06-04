package br.com.encontreinashopee.tracker

import android.content.Context
import br.com.encontreinashopee.model.OfferCardModel

interface OfferTrackerContract {

    fun clickOfferTracker(model: OfferCardModel, context: Context)

    fun clickNavegateOffer(model: OfferCardModel, context: Context)
}