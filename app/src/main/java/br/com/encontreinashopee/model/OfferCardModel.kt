package br.com.encontreinashopee.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferCardModel(
    val offerImage: String? = null,
    val offerTitle: String? = null,
    val urlOffer: String? = null,
    val identifier: String? = null,
    val offerPrice: String? = null,
    val id: String? = null
) : Parcelable
