package br.com.encontreinashopee.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferCardModel(
    val offerImage: String? = null,
    val offerTitle: String? = null,
    val urlOffer: String? = null
) : Parcelable
