package br.com.encontreinashopee.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferCardModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("offerImage")
    val offerImage: String,
    @SerializedName("offerTitle")
    val offerTitle: String,
    @SerializedName("urlOffer")
    val urlOffer: String,
    @SerializedName("offerPrice")
    val offerPrice: String,
    @SerializedName("description")
    val description: String
) : Parcelable
